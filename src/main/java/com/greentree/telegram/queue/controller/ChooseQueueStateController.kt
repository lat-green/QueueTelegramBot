package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.nothing
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class ChooseQueueStateController(
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queues = mutableMapOf<String, String>()

		for (queue in service.findAllQueue())
			queues.put(queue.name, "queue?queueId=${queue.id}")

		if (queues.isEmpty()){
			text("Очередей нет")
			redirect("main-menu")
		}

		sortedChoose("Выберите очередь", queues)
	}
}