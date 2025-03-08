package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService

class EnqueueByAdminStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO()

		executeInlineKeyboard("Выберете кого нужно добавить", service.findAllClientsName())

		onCallback {
			service.enqueue(service.findClientByName(it).chatId!!, queueId)

			redirect(next)
		}
	}
}