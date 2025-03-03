package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.DequeueState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class DequeueStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(!stateName.startsWith("dequeue-queue:"))
			return null
		val queueId = stateName.substring("dequeue-queue:".length).toLong()
		return DequeueState(mainService, queueId, "main-menu")
	}
}