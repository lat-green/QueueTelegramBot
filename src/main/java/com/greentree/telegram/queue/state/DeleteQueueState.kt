package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

class DeleteQueueState(val repository: QueueRepository, val mainService: MainService, val nextState: String) :
	ChatState {

	override fun init(sender: ChatSender) {
		createInlineKeyboard("Выберите очередь", mainService.findAllQueueNames(), sender)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): Nothing {
		mainService.deleteQueueByName(query.data)
		redirect(nextState)
	}
}
