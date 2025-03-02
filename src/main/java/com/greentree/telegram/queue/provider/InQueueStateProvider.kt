package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class InQueueStateProvider (val repository: PositionRepository) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "in-queue") return null

		val buttons = mapOf("Занять очередь" to "", "Освободить место" to "", "Просмотреть очередь" to "")
		return ChooseState("Выберете действие", buttons)
	}
}