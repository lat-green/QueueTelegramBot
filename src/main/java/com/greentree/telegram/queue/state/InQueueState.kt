package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import jakarta.transaction.Transactional
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@Transactional
class InQueueState(
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	enum class Actions(val text: String) {
		ENQUEUEFIRSTFREE("Занять"),
		ENQUEUEBYNUMBER("Занять по месту"),
		ENQUEUEDEADLIE("Занять дедлайн"),
		DEQUEUE("Освободить"),
		TOMAINMENU("В главное меню")
	}

	override fun init(sender: ChatSender): String? {
		sender.send(mainService.getQueuePeople(queueId))
		createInlineKeyboard(
			"Выберите действие",
			Actions.entries.map { it.text },
			sender
		)
		return null
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val text = query.data
		when(Actions.entries.first { it.text == text }) {
			Actions.DEQUEUE -> return "dequeue-queue:$queueId"

			Actions.ENQUEUEFIRSTFREE -> return "enqueue-first-free-queue:$queueId"

			Actions.ENQUEUEBYNUMBER -> return "enqueue-by-number-queue:$queueId"

			Actions.ENQUEUEDEADLIE -> return "enqueue-deadline:$queueId"

			Actions.TOMAINMENU -> return "main-menu"
		}
		return nextState
	}
}
