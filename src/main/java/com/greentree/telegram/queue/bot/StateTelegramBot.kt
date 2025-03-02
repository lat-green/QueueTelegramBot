package com.greentree.telegram.queue.bot

import com.greentree.telegram.queue.lib.StateMachine
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Slf4j
@Component
data class StateTelegramBot(
	private val botConfig: BotConfig,
	private val stateMachine: StateMachine,
) : TelegramLongPollingBot() {

	override fun getBotUsername(): String = botConfig.botName

	override fun getBotToken(): String = botConfig.token

	override fun onUpdateReceived(update: Update) {
		return stateMachine.onUpdateReceived(this, update)
	}
}
