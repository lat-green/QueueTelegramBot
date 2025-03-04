package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService

class EnqueueFirstFreeState(
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	override fun init(sender: ChatSender) {
		if(mainService.isClientInQueue(sender.chatId, queueId)) {
			sender.send("Вы уже в очереди")
			redirect("main-menu")
		}

		mainService.enqueue(sender.chatId, queueId)
		redirect(nextState)
	}
}
