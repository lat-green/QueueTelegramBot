package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.CreateQueueState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class CreateQueueStateProvider(val repository: QueueRepository, val mainService: MainService) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "create-queue") return null

		return CreateQueueState(repository, mainService, "main-menu")
	}
}