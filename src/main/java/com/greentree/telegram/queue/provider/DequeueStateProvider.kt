package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component

@Component
class DequeueStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(!stateName.startsWith("dequeue-queue:"))
			return null

		val queueId = stateName.substring("dequeue-queue:".length).toLong()
		return DequeueState(mainService, queueId, "main-menu")
	}
}