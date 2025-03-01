package com.greentree.telegram.queue.state

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class ChooseState(val text: String, val nextStates: Map<String, String?>) : ChatState {

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
		val text = query.data
		if(nextStates.containsKey(text))
			return nextStates[text]
		TODO("$sender $query")
	}

	override fun init(sender: ChatSender) {
		val keyboard = InlineKeyboardMarkup()
		val first_row: MutableList<InlineKeyboardButton> = ArrayList()
		val buttons = java.util.List.of<List<InlineKeyboardButton>>(first_row)
		for(name in nextStates.keys) {
			val button = InlineKeyboardButton()
			button.text = name
			button.callbackData = name
			first_row.add(button)
		}
		keyboard.keyboard = buttons
		val send = SendMessage(sender.chatId.toString(), text)
		send.replyMarkup = keyboard
		sender.sender.execute(send)
	}
}
