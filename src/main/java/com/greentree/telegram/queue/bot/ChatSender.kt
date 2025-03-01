package com.greentree.telegram.queue.bot

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

data class ChatSender(
	val sender: AbsSender,
	val chatId: Long,
)

fun ChatSender.send(text: String): Message? {
	return sender.execute(SendMessage(chatId.toString(), text))
}

fun AbsSender.withChatId(chatId: Long) = ChatSender(this, chatId)