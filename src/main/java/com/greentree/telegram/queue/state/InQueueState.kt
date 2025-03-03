package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService
import jakarta.transaction.Transactional
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@Transactional
class InQueueState(
	val mainService: MainService,
	val queueId: Long,
) : ChatState {

	enum class Actions(val text: String) {
		ENQUEUEFIRSTFREE("Занять"),
		ENQUEUEBYNUMBER("Занять по месту"),
		DEQUEUE("Освободить"),
		TOMAINMENU("В главное меню")
	}

	override fun init(sender: ChatSender) {
		sender.send(mainService.getQueuePeople(queueId))
		createInlineKeyboard(
			"Выберите действие",
			Actions.entries.map { it.text },
			sender
		)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): Nothing {
		val text = query.data
		redirect(
			when(Actions.entries.first { it.text == text }) {
				Actions.DEQUEUE -> "dequeue-queue:$queueId"
				Actions.ENQUEUEFIRSTFREE -> "enqueue-first-free-queue:$queueId"
				Actions.ENQUEUEBYNUMBER -> "enqueue-by-number-queue:$queueId"
				Actions.TOMAINMENU -> "main-menu"
			}
		)
	}
}
