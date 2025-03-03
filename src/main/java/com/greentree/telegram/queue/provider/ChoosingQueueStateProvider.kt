package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.SortedChooseState
import com.greentree.telegram.queue.state.StateProvider
import com.greentree.telegram.queue.state.send
import org.springframework.stereotype.Component

@Component
data class ChoosingQueueStateProvider(
	val repository: QueueRepository,
	val mainService: MainService
) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): StateProvider.Response? {
		if(stateName != "choosing-queue") return null
		val queues = mutableMapOf<String, String?>()
		for(queue in mainService.findAllQueue())
			queues[queue.name] = "queue:" + queue.id

		if(queues.isEmpty()) {
			return ChatState {
				it.send("Очередей нет")
				redirect("main-menu")
			}
		}

		return SortedChooseState("Выберите очередь", queues)
	}
}