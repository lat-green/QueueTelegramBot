package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.bot.ChatSender
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import org.springframework.stereotype.Component

@Component
data class ChoosingQueueStateProvider(
	val repository: QueueRepository,
) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "choosing-queue") return null
		val queues = mutableMapOf<String, String?>()

		for(queue in repository.findAll())
			queues[queue.name] = "queue:" + queue.id
		val choosingQueueState = ChooseState("Выберете предмет", queues)
		return choosingQueueState
	}
}