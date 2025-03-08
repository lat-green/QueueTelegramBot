package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class CreateQueueStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		text("Введите название новой очереди")

		onMessage {
			if(!service.createQueue(it.text, it.chatId))
				text("Очередь с таким названием уже существует")

			redirect(next)
		}
	}
}