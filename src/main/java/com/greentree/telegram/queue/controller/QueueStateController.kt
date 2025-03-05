package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.nothing
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService

class QueueStateController(
	val service: MainService,
) : StateController {

	enum class Actions(val text: String) {
		ENQUEUEFIRSTFREE("Занять"),
		ENQUEUEBYNUMBER("Занять по месту"),
		ENQUEUEDEADLIE("Занять дедлайн"),
		DEQUEUE("Освободить"),
		TOMAINMENU("В главное меню")
	}

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO("queueId not found")

		text(service.getQueuePeople(queueId))
		executeInlineKeyboard("Выберите действие", Actions.entries.map { it.text })

		onCallback { callbackData ->
			when(Actions.entries.first { it.text == callbackData }) {
				Actions.DEQUEUE -> redirect("dequeue?queueId=$queueId")

				Actions.ENQUEUEFIRSTFREE -> redirect("enqueue-first-free?queueId=$queueId")

				Actions.ENQUEUEBYNUMBER -> redirect("enqueue-by-number?queueId=$queueId")

				Actions.ENQUEUEDEADLIE -> redirect("enqueue-deadline?queueId=$queueId")

				Actions.TOMAINMENU -> redirect("main-menu")
			}
		}
	}
}