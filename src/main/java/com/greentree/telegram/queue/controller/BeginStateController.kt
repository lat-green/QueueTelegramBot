package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text

class BeginStateController(
	val next: String,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		text("Введите /start")

		onMessage {
			if(it.text == "/start")
				redirect(next)
			reInitialize()
		}
	}
}