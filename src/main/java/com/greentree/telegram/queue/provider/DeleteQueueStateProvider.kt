package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.model.Queue
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component

@Component
class DeleteQueueStateProvider (val repository: QueueRepository): StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): StateProvider.Response? {
		if(stateName != "delete-queue") return null

		val queues = mutableMapOf<String, String?>()
		for(queue in repository.findAll())
			queues[queue.name] = "queue:" + queue.id

		if (queues.isEmpty()){
			sender.send("Очередей нет")
			return redirect("main-menu")
		}
		return DeleteQueueState(repository, "main-menu")
	}
}