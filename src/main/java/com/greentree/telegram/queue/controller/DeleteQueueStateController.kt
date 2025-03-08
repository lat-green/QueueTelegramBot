package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService

class DeleteQueueStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		executeInlineKeyboard("Выберите удаляемую очередь", service.findAllQueueNames())

		onCallback {
			service.deleteQueueByName(it)

			redirect(next)
		}
	}
}