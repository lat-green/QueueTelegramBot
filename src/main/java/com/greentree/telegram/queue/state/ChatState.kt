package com.greentree.telegram.queue.state

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

interface ChatState : StateProvider.Response {

	fun onMessage(sender: AbsSender, message: Message): String? {
		sender.send(message.chatId, "error onMessage")
		return "begin"
	}

	fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
		sender.send(query.message.chatId, "error onCallback")
		return "begin"
	}

	fun init(sender: ChatSender)
}

fun AbsSender.send(chatId: Long, text: String): Message? {
	val message = SendMessage()
	message.setChatId(chatId)
	message.text = text
	return execute(message)
}