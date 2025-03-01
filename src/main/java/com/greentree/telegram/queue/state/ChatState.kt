package com.greentree.telegram.queue.state

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

interface ChatState {

	fun onMessage(sender: AbsSender, message: Message): String? {
		send(sender, message.chatId, "error onMessage")
		return "begin"
	}

	fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
		send(sender, query.message.chatId, "error onCallback")
		return "begin"
	}

	fun init(sender: AbsSender, chatId: Long)

	companion object {

		fun send(sender: AbsSender, chatId: Long, text: String) {
			val m = SendMessage()
			m.setChatId(chatId)
			m.text = text
			sender.execute(m)
		}
	}
}
