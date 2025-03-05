package com.greentree.telegram.queue.bot

import com.greentree.telegram.queue.lib.StateMachine
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
data class StateTelegramBot<S : Any>(
	private val botConfig: BotConfig,
	private val stateMachine: StateMachine<S>,
) : TelegramLongPollingBot(botConfig.token) {

	override fun getBotUsername(): String = botConfig.botName

	private val states = mutableMapOf<Long, S>()

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

	private fun resolveNextState(chatId: Long, state: S?) {
		var s = state
		while(s != null) {
			states[chatId] = s
			s = stateMachine.init(this, s, chatId)
		}
	}

	@Throws(TelegramApiException::class)
	private fun onMessage(message: Message) {
		val chatId = message.chatId
		val currentState = states.remove(chatId) ?: stateMachine.begin(chatId).also { stateMachine.init(this, it, chatId) }
		val nextStateName = stateMachine.onMessage(this, currentState, message)
		resolveNextState(chatId, nextStateName)
	}

	@Throws(TelegramApiException::class)
	private fun onCallback(query: CallbackQuery) {
		val chatId = query.message.chatId
		val currentState = states.remove(chatId) ?: stateMachine.begin(chatId).also { stateMachine.init(this, it, chatId) }
		val nextStateName = stateMachine.onCallback(this, currentState, query)
		resolveNextState(chatId, nextStateName)
	}
}
