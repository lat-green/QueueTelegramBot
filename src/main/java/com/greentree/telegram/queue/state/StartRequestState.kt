package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.entity.Client
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.service.MainService
import lombok.extern.slf4j.Slf4j
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Slf4j
data class StartRequestState(val repository: ClientRepository,
							 val mainService: MainService,
							 val nextState: String) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message): String? {
		val text = message.text
		if("/start" == text) {
			mainService.addClient(message.chatId)
			return nextState
		}
		return null
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите /start")
	}
}
