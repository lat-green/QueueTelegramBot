package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.InQueueState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class InQueueStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(!stateName.startsWith("queue:"))
			return null
		val queueId = stateName.substring("queue:".length).toLong()
		return InQueueState(mainService, queueId)
	}
}