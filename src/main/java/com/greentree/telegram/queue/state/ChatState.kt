package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.redirect
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

fun interface ChatState : StateProvider.Response {

	fun onMessage(sender: AbsSender, message: Message) {
		sender.send(message.chatId, "error onMessage")
		redirect("begin")
	}

	fun onCallback(sender: AbsSender, query: CallbackQuery) {
		sender.send(query.message.chatId, "error onCallback")
		redirect("begin")
	}

	fun init(sender: ChatSender)
}

fun AbsSender.send(chatId: Long, text: String): Message? {
	val message = SendMessage()
	message.setChatId(chatId)
	message.text = text
	return execute(message)
}