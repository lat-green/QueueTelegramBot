package com.greentree.telegram.queue.lib

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

interface StateMachine<S : Any> {

	fun begin(chatId: Long): S

	fun init(sender: AbsSender, currentState: S, chatId: Long): S?

	fun onMessage(sender: AbsSender, currentState: S, message: Message): S?
	fun onCallback(sender: AbsSender, currentState: S, callback: CallbackQuery): S?
}
