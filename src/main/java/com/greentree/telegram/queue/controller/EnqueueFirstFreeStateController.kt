package com.greentree.telegram.queue.controller

import com.greentree.commons.util.react.refresh
import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.nothing
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class EnqueueFirstFreeStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO()

		if (service.isClientInQueue(chatId, queueId)){
			text("Вы уже в очереди")

			redirect("main-menu")
		}

		service.enqueue(chatId, queueId)

		redirect(next)
	}
}