package com.greentree.telegram.queue.controller

import com.greentree.commons.util.react.refresh
import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.*
import com.greentree.telegram.queue.service.MainService

class EnqueueByNumberStateController(
	val next: String,
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO()

		if (service.isClientInQueue(chatId, queueId)){
			text("Вы уже в очереди")

			redirect("main-menu")
		}

		text("Введите желаемый номер")

		onMessage {
			fun invalidNumber() : Nothing {
				text("Невозможное значение ${it.text}")
				reInitialize()
			}
			val number = try {
				  it.text.toInt()
			} catch (e: NumberFormatException){
				invalidNumber()
			}
			if (number <= 0){
				invalidNumber()
			}
			service.enqueueByNumber(chatId, queueId, number)

			redirect(next)
		}
	}
}