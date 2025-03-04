package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService

class DequeueState(
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	override fun init(sender: ChatSender): Nothing {
		if(!mainService.isClientInQueue(sender.chatId, queueId)) {
			sender.send("Вы не в очереди")
			redirect("main-menu")
		}

		mainService.dequeue(sender.chatId, queueId)
		redirect(nextState)
	}
}
