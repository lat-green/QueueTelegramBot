package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class DequeueByAdminStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO()
		val positions = service.getAllPositionByQueueId(queueId)

		if (positions.isEmpty()){
			text("Очередь пуста")
			redirect(next)
		}

		executeInlineKeyboard("Выберете кого нужно убрать", service.findAllNumbersAndNamesByQueueId(queueId))

		onCallback {
			val index = it.indexOf(")")
			val name = it.substring(index + 1)
			val client = service.findClientByName(name)

			client.chatId?.let { it1 -> service.dequeue(it1, queueId) }

			redirect(next)
		}
	}
}