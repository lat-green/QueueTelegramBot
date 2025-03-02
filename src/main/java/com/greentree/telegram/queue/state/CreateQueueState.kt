package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.model.Queue
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class CreateQueueState(val repository: QueueRepository, val mainService: MainService, val nextState: String) : ChatState {

	override fun init(sender: ChatSender) {
		sender.send("Отправь название очереди")
	}

	override fun onMessage(sender: AbsSender, message: Message): String {
		if(!mainService.createQueue(message.text, message.chatId))
			sender.send(message.chatId, "Очередь с таким названием уже существует")
		return "main-menu"
	}
}
