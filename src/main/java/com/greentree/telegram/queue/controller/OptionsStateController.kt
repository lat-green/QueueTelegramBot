package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class OptionsStateController() : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val buttons = mapOf("Изменить имя" to "rename", "В главное меню" to "main-menu")

		choose("Опции", buttons)
	}
}