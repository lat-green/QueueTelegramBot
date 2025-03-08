package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.service.MainService

class MainMenuStateController(
	val service: MainService
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val client = service.findClientByChatId(chatId)
		val buttons = mutableMapOf(
			"Очереди" to "choose-queue",
			"Опции" to "options"
		)

		if(client.isAdmin) {
			buttons.put("Создание очереди", "create-queue")
			buttons.put("Удаление очереди", "delete-queue")
		}

		choose("Главное меню", buttons)
	}
}