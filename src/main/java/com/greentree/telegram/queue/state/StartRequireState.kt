package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService
import lombok.extern.slf4j.Slf4j
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Slf4j
data class StartRequireState(
	val mainService: MainService,
	val nextState: String
) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message) {
		val text = message.text
		if("/start" == text) {
			val user = message.from
			val name = "${user.firstName} ${user.lastName}"
			mainService.addClient(message.chatId, name)
			redirect(nextState)
		}
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите /start")
	}
}
