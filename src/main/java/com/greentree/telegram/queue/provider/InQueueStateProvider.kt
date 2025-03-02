package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component

@Component
class InQueueStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(!stateName.startsWith("queue:"))
			return null

		val queueId = stateName.substring("queue:".length).toLong()
		return InQueueState(mainService, queueId, "main-menu")
	}
}