package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.EnqueueFirstFreeState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class EnqueueFirstFreeStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(!stateName.startsWith("enqueue-first-free-queue:")) return null
		val queueId = stateName.substring("enqueue-first-free-queue:".length).toLong()
		return EnqueueFirstFreeState(mainService, queueId, "main-menu")
	}
}