package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class OptionsState(val mainService: MainService, val nextState: String) : ChatState {

	enum class Options(val text: String){
		RENAME("Изменить имя"),
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
		val text = query.data
		val chatId = query.message.chatId

		when(Options.entries.first { it.text == text }) {
			Options.RENAME -> return "rename"
		}
		return null
	}

	override fun init(sender: ChatSender): String? {
		createInlineKeyboard("Выберите опцию", Options.entries.map { it.text }, sender)
		return null
	}
}
