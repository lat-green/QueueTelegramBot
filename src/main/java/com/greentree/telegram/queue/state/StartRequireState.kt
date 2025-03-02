package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.service.MainService
import lombok.extern.slf4j.Slf4j
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Slf4j
data class StartRequireState(
	val mainService: MainService,
	val nextState: String
) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message): String? {
		val text = message.text
		if("/start" == text) {
			val user = message.from
			val name = "${user.firstName} ${user.lastName}"
			mainService.addClient(message.chatId, name)
			return nextState
		}
		return null
	}

	override fun init(sender: ChatSender): String? {
		sender.send("Введите /start")
		return null
	}
}
