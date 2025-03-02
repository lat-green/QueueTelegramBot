package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
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
		createInlineKeyboard(text, nextStates, sender)
	}
}
