package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController

class OptionsStateController : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val buttons = mapOf("Изменить имя" to "rename", "В главное меню" to "main-menu")

		choose("Опции", buttons)
	}
}