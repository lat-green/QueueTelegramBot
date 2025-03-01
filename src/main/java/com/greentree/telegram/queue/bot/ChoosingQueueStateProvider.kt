package com.greentree.telegram.queue.bot

import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class ChoosingQueueStateProvider(
	val repository: QueueRepository,
) : StateProvider {

	override fun findOrNull(stateName: String): ChatState? {
		TODO("Not yet implemented")
	}
}