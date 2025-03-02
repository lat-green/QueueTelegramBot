package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.model.Queue
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class InQueueState(val repository: PositionRepository, val nextState: String) : ChatState {
    override fun init(sender: ChatSender) {
        val positions = repository.findAll()

        createInlineKeyboard("Выберете каким способом записаться записаться", mapOf("В конец очереди" to "main-menu", "По номеру места" to "main-menu"), sender)
    }

    override fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
        val text = query.data
        return super.onCallback(sender, query)
    }
}
