package com.greentree.telegram.queue.bot

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.Redirect
import com.greentree.telegram.queue.state.StateProvider
import com.greentree.telegram.queue.state.withChatId
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.*
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Slf4j
@Component
data class StateTelegramBot(
	private val botConfig: BotConfig,
	private val stateProviders: List<StateProvider>,
) : TelegramLongPollingBot() {

	override fun getBotUsername(): String = botConfig.botName

	private val states: MutableMap<Long, StateInfo> = HashMap()
	override fun getBotToken(): String = botConfig.token

	override fun onUpdateReceived(update: Update) {
		try {
			if(update.hasMessage() && update.message.hasText()) {
				val message = update.message
				onMessage(message)
			}
			if(update.hasCallbackQuery()) {
				val query = update.callbackQuery
				onCallback(query)
			}
		} catch(e: TelegramApiException) {
			log.error("", e)
		}
	}

	private tailrec fun resolveNextState(chatId: Long, stateName: String?) {
		if(stateName != null) {
			val nextStateInfo = find(chatId, stateName)
			states[chatId] = nextStateInfo
			val nextStateName = nextStateInfo.state.init(withChatId(chatId))
			resolveNextState(chatId, nextStateName)
		}
	}

	@Throws(TelegramApiException::class)
	private fun onMessage(message: Message) {
		val chatId = message.chatId
		val currentStateInfo = states.remove(chatId) ?: begin(chatId)
		val (_, state) = currentStateInfo
		val nextStateName = state.onMessage(this, message)
		resolveNextState(chatId, nextStateName)
	}

	@Throws(TelegramApiException::class)
	private fun onCallback(query: CallbackQuery) {
		val chatId = query.message.chatId
		val currentStateInfo = states.remove(chatId) ?: begin(chatId)
		val (_, state) = currentStateInfo
		val nextStateName = state.onCallback(this, query)
		resolveNextState(chatId, nextStateName)
	}

	private fun begin(chatId: Long) = find(chatId, "begin")

	private fun find(chatId: Long, stateName: String): StateInfo {
		val response = stateProviders.asSequence().mapNotNull {
			it.findOrNull(withChatId(chatId), stateName)
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
