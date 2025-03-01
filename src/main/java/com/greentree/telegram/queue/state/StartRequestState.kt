package com.greentree.telegram.queue.state

import lombok.extern.slf4j.Slf4j
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Slf4j
data class StartRequestState(val nextState: String) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message): String? {
		val text = message.text
		if("/start" == text)
			return nextState
		return null
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите /start")
	}
}
