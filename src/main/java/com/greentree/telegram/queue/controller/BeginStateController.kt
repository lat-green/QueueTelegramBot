package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.*
import com.greentree.telegram.queue.service.MainService

class BeginStateController(
	val next: String,
	val service: MainService
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		onMessage {
			if(it.text == "/start") {
				val user = it.from
				val name = "${user.firstName} ${user.lastName}"

				service.addClient(it.chatId, name)

				redirect(next)
			}
			redirect("begin")
		}
	}
}