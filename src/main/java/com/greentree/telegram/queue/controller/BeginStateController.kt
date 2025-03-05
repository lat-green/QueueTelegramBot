package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class BeginStateController(
	val mainService: MainService,
	val next: String,
	val service: MainService
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		text("Введите /start")

		onMessage {
			if(it.text == "/start"){
				val user = it.from
				val name = "${user.firstName} ${user.lastName}"

				service.addClient(it.chatId, name)

				redirect(next)
			}

			reInitialize()
		}
	}
}