package com.greentree.telegram.queue.lib

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.Redirect
import com.greentree.telegram.queue.state.StateProvider
import com.greentree.telegram.queue.state.withChatId
import org.hibernate.query.sqm.tree.SqmNode.*
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class StateMachineImpl(
	private val stateProviders: List<StateProvider>,
) : StateMachine<StateMachineImpl.StateInfo> {

	override fun onCallback(sender: AbsSender, currentState: StateInfo, callback: CallbackQuery): StateInfo? {
		val chatId = callback.message.chatId
		try {
			currentState.state.onCallback(sender, callback)
		} catch(_: ReInitializeException) {
			return currentState
		} catch(e: RedirectException) {
			return find(chatId, e.nextStateName)
		} catch(_: NothingException) {
			return null
		}
		return null
	}

	override fun onMessage(sender: AbsSender, currentState: StateInfo, message: Message): StateInfo? {
		val chatId = message.chatId
		try {
			currentState.state.onMessage(sender, message)
		} catch(_: ReInitializeException) {
			return currentState
		} catch(e: RedirectException) {
			return find(chatId, e.nextStateName)
		} catch(_: NothingException) {
			return null
		}
		return null
	}

	override fun init(sender: AbsSender, currentState: StateInfo, chatId: Long): StateInfo? {
		try {
			currentState.state.init(sender.withChatId(chatId))
		} catch(_: ReInitializeException) {
			return currentState
		} catch(e: RedirectException) {
			return find(chatId, e.nextStateName)
		} catch(_: NothingException) {
			return null
		}
		return null
	}

	override fun begin(chatId: Long) = find(chatId, "begin")

	private fun find(chatId: Long, stateName: String): StateInfo {
		val response = stateProviders.asSequence().mapNotNull {
			it.findOrNull(chatId, stateName)
		}.singleOrNull() ?: run {
			if(stateName != "begin") {
				log.error("state $stateName not found or duplicate")
				return begin(chatId)
			} else {
				TODO(stateName)
			}
		}
		return when(response) {
			is ChatState -> StateInfo(stateName, response)
			is Redirect -> {
				if(response.nextStateName != stateName)
					find(chatId, response.nextStateName)
				else
					TODO(stateName)
			}
		}
	}

	data class StateInfo(
		val name: String,
		val state: ChatState,
	)
}