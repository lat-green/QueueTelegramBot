package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.EnqueueDeadlineState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class EnqueueDeadlineStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(!stateName.startsWith("enqueue-deadline:")) return null
		val queueId = stateName.substring("enqueue-deadline:".length).toLong()
		return EnqueueDeadlineState(mainService, queueId, "main-menu")
	}
}