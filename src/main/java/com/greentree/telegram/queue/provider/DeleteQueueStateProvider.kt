package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.DeleteQueueState
import com.greentree.telegram.queue.state.StateProvider
import com.greentree.telegram.queue.state.redirect
import com.greentree.telegram.queue.state.send
import org.springframework.stereotype.Component

@Component
class DeleteQueueStateProvider(val repository: QueueRepository, val mainService: MainService) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): StateProvider.Response? {
		if(stateName != "delete-queue") return null

		return DeleteQueueState(repository, mainService, "main-menu")
	}
}