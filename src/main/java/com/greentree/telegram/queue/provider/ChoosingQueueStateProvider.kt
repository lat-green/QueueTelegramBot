package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component

@Component
data class ChoosingQueueStateProvider(
	val repository: QueueRepository,
	val mainService: MainService
) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): StateProvider.Response? {
		if(stateName != "choosing-queue") return null

		val queues = mutableMapOf<String, String?>()
		for(queue in mainService.findAllQueue())
			queues[queue.name] = "queue:" + queue.id

		if (queues.isEmpty()){
			sender.send("Очередей нет")
			return redirect("main-menu")
		}
		return SortedChooseState("Выберете очередь", queues)
	}
}