package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.nothing
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@JvmRecord
data class RenameState(val mainService: MainService, val nextState: String) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message): Nothing {
		val chatId = message.chatId
		val text = message.text
		mainService.rename(text, chatId)
		redirect(nextState)
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите новое имя")
	}
}
