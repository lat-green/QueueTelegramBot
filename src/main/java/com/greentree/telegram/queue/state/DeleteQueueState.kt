package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

class DeleteQueueState(val repository: QueueRepository, val mainService: MainService, val nextState: String) : ChatState {

	override fun init(sender: ChatSender): String? {
		createInlineKeyboard("Выберете очередь", mainService.findAllQueueNames(), sender)
		return null
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		mainService.deleteQueueByName(query.data)
		return nextState
	}
}
