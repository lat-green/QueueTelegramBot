package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.model.Queue
import com.greentree.telegram.queue.repository.QueueRepository
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class CreateQueueState(val repository: QueueRepository, val nextState: String) : ChatState {
    override fun init(sender: ChatSender) {
        sender.send("Отправь название очереди")
    }

    override fun onMessage(sender: AbsSender, message: Message): String? {
        if (repository.findByName(message.text).isEmpty) {
            val queue = Queue()
            queue.name = message.text
            repository.save(queue)
        } else {
            sender.send(message.chatId, "Очередь с таким названием уже существует")
        }
        return "main-menu"
    }
}
