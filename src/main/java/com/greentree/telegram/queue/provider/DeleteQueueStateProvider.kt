package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.DeleteQueueState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class DeleteQueueStateProvider(val repository: QueueRepository, val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): StateProvider.Response? {
		if(stateName != "delete-queue") return null

		return DeleteQueueState(repository, mainService, "main-menu")
	}
}