package com.greentree.telegram.queue.bot

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StateProvider
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
		var currentStateInfo = states.remove(chatId) ?: begin()
		val (_, state) = currentStateInfo
		var nextStateName = state.onMessage(this, message)
		val nextStateInfo: StateInfo
		if(nextStateName != null) {
			nextStateInfo = find(nextStateName)
			states[chatId] = nextStateInfo
		} else {
			nextStateInfo = currentStateInfo
		}
		nextStateInfo.state.init(this, chatId)
	}

	@Throws(TelegramApiException::class)
	private fun onCallback(query: CallbackQuery) {
		val chatId = query.message.chatId
		var currentStateInfo = states.remove(chatId) ?: begin()
		val (_, state) = currentStateInfo
		var nextStateName = state.onCallback(this, query)
		val nextStateInfo: StateInfo
		if(nextStateName != null) {
			nextStateInfo = find(nextStateName)
			states[chatId] = nextStateInfo
		} else {
			nextStateInfo = currentStateInfo
		}
		nextStateInfo.state.init(this, chatId)
	}

	private fun begin() = find("begin")

	private fun find(stateName: String): StateInfo {
		val state = stateProviders.asSequence().mapNotNull {
			it.findOrNull(this, stateName)
		}.singleOrNull() ?: run {
			if(stateName != "begin") {
				log.error("state $stateName not found or duplicate")
				return begin()
			} else {
				TODO(stateName)
			}
		}
		return StateInfo(stateName, state)
	}

	data class StateInfo(
		val name: String,
		val state: ChatState,
	)
}
