package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.reInitialize
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class OptionsStateController() : StateController {

	enum class Options(val text: String, val nextState: String) {
		RENAME("Изменить имя", "rename"),
		TOMAINMENU("В главное меню", "main-menu"),
	}

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		executeInlineKeyboard("Выберите опцию", Options.entries.map { it.text })

		onCallback {
			redirect(it)
		}
	}
}