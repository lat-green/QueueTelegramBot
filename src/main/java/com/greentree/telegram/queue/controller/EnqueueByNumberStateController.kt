package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class EnqueueByNumberStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong() ?: TODO()

		if(service.isClientInQueue(chatId, queueId)) {
			text("Вы уже в очереди")

			redirect("main-menu")
		}

		text("Введите желаемый номер")

		onMessage {
			val number = it.text.toInt()

			if(number <= 0) {
				text("Невозможное значение")

				reInitialize()
			}
			service.enqueueByNumber(chatId, queueId, number)

			redirect(next)
		}
	}
}