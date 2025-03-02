package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.entity.Client
import com.greentree.telegram.queue.entity.Position
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import jakarta.transaction.Transactional
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@Transactional
class InQueueState(
	val positionRepository: PositionRepository,
	val queueRepository: QueueRepository,
	val clientRepository: ClientRepository,
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	enum class Actions(val text: String) {
		ENQUEUEFIRSTFREE("Занять"),
		ENQUEUEBYNUMBER("Занять по месту"),
		DEQUEUE("Освободить")
	}

	override fun init(sender: ChatSender) {
		sender.send(mainService.getQueuePeople(queueId))
		createInlineKeyboard(
			"Выберете действие",
			Actions.entries.map { it.text },
			sender
		)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val text = query.data
		when(Actions.entries.first { it.text == text }) {
			Actions.DEQUEUE -> {
				if(!mainService.dequeue(query.message.chatId, queueId))
					sender.send(query.message.chatId, "Вы не находитесь в очереди")
			}
			Actions.ENQUEUEFIRSTFREE -> {
				if(!mainService.enqueue(query.message.chatId, queueId))
					sender.send(query.message.chatId, "Вы уже в очереди")
			}

			Actions.ENQUEUEBYNUMBER -> return "enqueue-by-number"
		}
		return nextState
	}
}
