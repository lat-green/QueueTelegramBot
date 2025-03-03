package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueDeadlineState(
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	override fun init(sender: ChatSender): String? {
		if(mainService.isClientInQueue(sender.chatId, queueId)) {
			sender.send("Вы уже в очереди")
			return "main-menu"
		}

		mainService.enqueueDeadline(sender.chatId, queueId)
		return nextState
	}
}
