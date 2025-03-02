package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class InQueueStateProvider(val repository: PositionRepository) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(!stateName.startsWith("queue:"))
			return null
		val queueId = Integer.parseInt(stateName.substring("queue:".length))
		val buttons = mapOf("Занять очередь" to "", "Освободить место" to "", "Просмотреть очередь" to "")
		return ChooseState("Выберете действие", buttons)
	}
}