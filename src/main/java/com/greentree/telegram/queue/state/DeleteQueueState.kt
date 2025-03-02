package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.repository.QueueRepository
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

class DeleteQueueState(val repository: QueueRepository, val nextState: String) : ChatState {

	override fun init(sender: ChatSender) {
		createInlineKeyboard("Выберете очередь", repository.findAll().map { it.name }, sender)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val name = query.data
		repository.deleteByName(name)
		return nextState
	}
}
