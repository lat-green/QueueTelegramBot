package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.lib.redirect
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class ChooseState(val text: String, val nextStates: Map<String, String?>) : ChatState {

	override fun onCallback(sender: AbsSender, query: CallbackQuery) {
		val text = query.data
		if(nextStates.containsKey(text))
			redirect(nextStates[text])
		sender.send(query.message.chatId, "Выберите одно из представленных действий")
	}

	override fun init(sender: ChatSender) {
		createInlineKeyboard(text, nextStates.keys, sender)
	}
}
