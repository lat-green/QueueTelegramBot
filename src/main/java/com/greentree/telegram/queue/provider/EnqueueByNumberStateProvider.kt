package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.EnqueueByNumberState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class EnqueueByNumberStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(!stateName.startsWith("enqueue-by-number-queue:")) return null
		val queueId = stateName.substring("enqueue-by-number-queue:".length).toLong()
		return EnqueueByNumberState(mainService, queueId, "main-menu")
	}
}