package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class OptionsState(val mainService: MainService, val nextState: String) : ChatState {

	enum class Options(val text: String, val nextState: String) {
		RENAME("Изменить имя", "rename"),
		TOMAINMENU("В главное меню", "main-menu"),
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): Nothing {
		val text = query.data

		redirect(Options.entries.first { it.text == text }.nextState)
	}

	override fun init(sender: ChatSender) {
		createInlineKeyboard("Выберите опцию", Options.entries.map { it.text }, sender)
	}
}
