package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class ChooseState(val text: String, val nextStates: Map<String, String?>) : ChatState {

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
		val text = query.data
		if(nextStates.containsValue(text))
			return nextStates[text]
		sender.send(query.message.chatId, "Выберите одно из представленных действий")
		return null
	}

	override fun init(sender: ChatSender) {
		createInlineKeyboard(text, nextStates.keys, sender)
	}
}
