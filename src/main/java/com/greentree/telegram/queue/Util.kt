package com.greentree.telegram.queue

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.state.ChatSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun createInlineKeyboard(text: String, buttons: Iterable<String>, sender: ChatSender) {
	val keyboard = InlineKeyboardMarkup()
	keyboard.keyboard = buttons.map { text ->
		val button = InlineKeyboardButton()
		button.text = text
		button.callbackData = text
		listOf(button)
	}
	val send = SendMessage(sender.chatId.toString(), text)
	send.replyMarkup = keyboard
	sender.sender.execute(send)
}

fun StateController.Context.executeInlineKeyboard(text: String, buttons: Iterable<String>) {
	val keyboard = InlineKeyboardMarkup()
	keyboard.keyboard = buttons.map { text ->
		val button = InlineKeyboardButton()
		button.text = text
		button.callbackData = text
		listOf(button)
	}
	val send = SendMessage(chatId.toString(), text)
	send.replyMarkup = keyboard
	execute(send)
}