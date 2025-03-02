package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.model.Queue
import com.greentree.telegram.queue.repository.QueueRepository
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class DeleteQueueState(val repository: QueueRepository, val nextState: String) : ChatState {
    override fun init(sender: ChatSender) {
        val queues = mutableMapOf<String, String?>()
        for(queue in repository.findAll())
            queues[queue.name] = "queue:" + queue.id
        createInlineKeyboard("Выберете очередь", queues, sender)
    }

    override fun onCallback(sender: AbsSender, query: CallbackQuery): String? {
        val text = query.data
        repository.findByName(text).ifPresent() { queue -> repository.delete(queue)}
        return nextState
    }
}
