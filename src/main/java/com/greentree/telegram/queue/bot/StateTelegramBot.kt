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

	private val states: MutableMap<Long, StateInfo> = HashMap()

	override fun getBotUsername(): String = botConfig.botName

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

	@Throws(TelegramApiException::class)
	private fun onMessage(message: Message) {
		val chatId = message.chatId
		var currentStateInfo = states.remove(chatId) ?: begin(chatId)
		val (_, state) = currentStateInfo
		var nextStateName = state.onMessage(this, message)
		val nextStateInfo: StateInfo
		if(nextStateName != null) {
			nextStateInfo = find(chatId, nextStateName)
			states[chatId] = nextStateInfo
		} else {
			nextStateInfo = currentStateInfo
		}
		nextStateInfo.state.init(withChatId(chatId))
	}

	@Throws(TelegramApiException::class)
	private fun onCallback(query: CallbackQuery) {
		val chatId = query.message.chatId
		var currentStateInfo = states.remove(chatId) ?: begin(chatId)
		val (_, state) = currentStateInfo
		var nextStateName = state.onCallback(this, query)
		val nextStateInfo: StateInfo
		if(nextStateName != null) {
			nextStateInfo = find(chatId, nextStateName)
			states[chatId] = nextStateInfo
		} else {
			nextStateInfo = currentStateInfo
		}
		nextStateInfo.state.init(withChatId(chatId))
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
			is Redirect -> find(chatId, response.nextStateName)
		}
	}

	data class StateInfo(
		val name: String,
		val state: ChatState,
	)
}
