package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery


class QueueStateController(
	val service: MainService,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		val queueId = params.get("queueId")?.toLong()?: TODO("queueId not found")
		val buttons = mutableMapOf(
			"Занять" to "enqueue-first-free?queueId=$queueId",
			"Занять по месту" to "enqueue-by-number?queueId=$queueId",
			"Занять дедлайн" to "enqueue-deadline?queueId=$queueId",
			"Освободить" to "dequeue?queueId=$queueId")
		if (service.findClientByChatId(chatId).isAdmin){
			buttons.put("Добавить кого-либо в очередь", "enqueue-by-admin?queueId=$queueId")
			buttons.put("Убрать кого-либо из очереди", "dequeue-by-admin?queueId=$queueId")
		}
		buttons.put("В главное меню", "main-menu")

		text(service.getQueuePeople(queueId))

		choose("Выберите действие", buttons)
	}
}