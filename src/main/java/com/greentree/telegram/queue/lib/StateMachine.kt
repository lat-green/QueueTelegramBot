package com.greentree.telegram.queue.lib

import com.greentree.commons.util.react.FlagReactContextProvider
import com.greentree.commons.util.react.ReactContext
import com.greentree.commons.util.react.ReactContextProvider
import com.greentree.telegram.queue.lib.StateMachine.*
import com.greentree.telegram.queue.lib.argument.MapArgumentResolver
import com.greentree.telegram.queue.lib.argument.MethodCaller
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.RedirectException
import com.greentree.telegram.queue.state.send
import com.greentree.telegram.queue.state.withChatId
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.*
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.reflect.Method

@Slf4j
@Component
data class StateMachine(
	private val methodCaller: MethodCaller,
) {

	private val states = mutableMapOf<Long, StateInfo>()
	private val stateHandlers = mutableSetOf<StateHandlerProvider>()

	fun registerStateHandler(stateName: String, method: Method) {
		stateHandlers.add(StateHandlerProvider(stateName, StateHandlerImpl(method)))
	}

	fun onUpdateReceived(sender: AbsSender, update: Update) {
		if(stateHandlers.isEmpty())
			return
		try {
			if(update.hasMessage() && update.message.hasText()) {
				val message = update.message
				onMessage(sender, message)
			}
			if(update.hasCallbackQuery()) {
				val query = update.callbackQuery
				onCallback(sender, query)
			}
		} catch(e: TelegramApiException) {
			log.error("", e)
		}
	}

	private tailrec fun resolveNextState(sender: AbsSender, chatId: Long, nextStateName: String?) {
		if(nextStateName != null) {
			val nextStateInfo = find(nextStateName)
			states[chatId] = nextStateInfo
			val a = nextStateInfo.init(methodCaller, sender.withChatId(chatId))
			resolveNextState(sender, chatId, a)
		}
	}

	private fun onMessage(sender: AbsSender, message: Message) {
		val chatId = message.chatId
		val currentState = states.remove(chatId) ?: begin()
		val nextStateName = currentState.onMessage(methodCaller, sender, message)
		resolveNextState(sender, chatId, nextStateName)
	}

	private fun onCallback(sender: AbsSender, query: CallbackQuery) {
		val chatId = query.message.chatId
		val currentState = states.remove(chatId) ?: begin()
		val nextStateName = currentState.onCallback(methodCaller, sender, query)
		resolveNextState(sender, chatId, nextStateName)
	}

	private fun begin() = find("begin")

	private fun find(stateName: String): StateInfo = StateInfo(stateName, stateHandlers.asSequence().mapNotNull {
		it.findOrNull(stateName)
	}.singleOrNull() ?: run {
		if(stateName != "begin") {
			log.error("state $stateName not found or duplicate")
			return begin()
		} else {
			TODO("begin not found")
		}
	})

	data class StateInfo(
		val name: String,
		val stateHandler: StateHandler,
	) {

		private val context: ReactContextProvider = FlagReactContextProvider()

		private fun nextStateContext(): StateContext = object : StateContext, ReactContext by context.next() {
		}

		private fun result(sender: ChatSender, block: () -> Any?): String? {
			val result = try {
				block()
			} catch(e: RedirectException) {
				return e.nextStateName
			}
			when(result) {
				is String -> {
					sender.send(result)
				}

				null -> {}
				Unit -> {}
				else -> {
					TODO("$result")
				}
			}
			return null
		}

		fun onMessage(methodCaller: MethodCaller, sender: AbsSender, message: Message): String? {
			val sender = sender.withChatId(message.chatId)
			val mc = methodCaller.builder().apply {
				add(
					MapArgumentResolver(
						mapOf(
							"chatId" to message.chatId,
							"message" to message,
							"chat-sender" to sender.sender,
							"abs-sender" to sender.sender,
							"context" to nextStateContext(),
							"type" to "onMessage",
						)
					)
				)
			}.build()
			return result(sender) {
				stateHandler.call(mc)
			}
		}

		fun onCallback(methodCaller: MethodCaller, sender: AbsSender, query: CallbackQuery): String? {
			val sender = sender.withChatId(query.message.chatId)
			val mc = methodCaller.builder().apply {
				add(
					MapArgumentResolver(
						mapOf(
							"chatId" to query.message.chatId,
							"query" to query,
							"chat-sender" to sender.sender,
							"abs-sender" to sender.sender,
							"context" to nextStateContext(),
							"type" to "onCallback",
						)
					)
				)
			}.build()
			return result(sender) {
				stateHandler.call(mc)
			}
		}

		fun init(methodCaller: MethodCaller, sender: ChatSender): String? {
			val mc = methodCaller.builder().apply {
				add(
					MapArgumentResolver(
						mapOf(
							"chatId" to sender.chatId,
							"chat-sender" to sender.sender,
							"abs-sender" to sender.sender,
							"context" to nextStateContext(),
							"type" to "init",
						)
					)
				)
			}.build()
			return result(sender) {
				stateHandler.call(mc)
			}
		}
	}

	data class StateHandlerProvider(
		val stateName: String,
		val stateHandler: StateHandler,
	) {

		fun findOrNull(stateName: String): StateHandler? {
			if(this.stateName == stateName)
				return stateHandler
			val list = stateName.split("?")
			if(list.size != 2 || list[0] != this.stateName)
				return null
			val map = mutableMapOf<String, String>()
			val paramsPart = list[1].split("&")
			for(paramPair in paramsPart) {
				val (key, value) = paramPair.split("=")
				map[key] = value
			}
			return StateHandler {
				val methodCaller = it.builder().add(MapArgumentResolver(map)).build()
				stateHandler.call(methodCaller)
			}
		}
	}

	fun interface StateHandler {

		fun call(methodCaller: MethodCaller): Any?
	}

	data class StateHandlerImpl(
		val method: Method,
	) : StateHandler {

		override fun call(methodCaller: MethodCaller): Any? {
			return methodCaller.call(method)
		}
	}
}
