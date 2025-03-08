package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class RenameStateController(
	val next: String,
	val service: MainService
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		text("Введите новое имя")

		onMessage {
			val newName = it.text

			if (newName == null){
				text("Невозможное имя")
				redirect("main-menu")
			}

			service.rename(newName, it.chatId)

			redirect(next)
		}
	}
}