package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.createInlineKeyboard
import com.greentree.telegram.queue.model.Client
import com.greentree.telegram.queue.model.Position
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

	val queue = queueRepository.findById(queueId).orElseThrow()
	val positions = positionRepository.findAllByQueue(queue)

	enum class Actions(val text: String) {
		ENQUEUEFIRSTFREE("Занять"),
		ENQUEUEBYNUMBER("Занять по месту"),
		DEQUEUE("Освободить")
	}

	override fun init(sender: ChatSender) {
		positions.sortBy { it.number }
		val builder = StringBuilder()
		builder.append(queue.name)
		builder.append('\n')
		for(position in positions) {
			builder.append(position.number)
			builder.append(")")
			builder.append(position.client!!.name)
			builder.append('\n')
		}
		sender.send(builder.toString())
		if(positions.isEmpty())
			sender.send("Очередь пуста")
		createInlineKeyboard(
			"Выберете действие",
			Actions.entries.map { it.text },
			sender
		)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val text = query.data
		val client = clientRepository.findByChatId(query.message.chatId).orElseThrow()
		when(Actions.entries.first { it.text == text }) {
			Actions.DEQUEUE -> {
				if(isClientInQueue(positions, client)) {
					mainService.dequeue(queueId, client.id!!)
				} else {
					sender.send(query.message.chatId, "Вы не находитесь в очереди")
				}
			}

			Actions.ENQUEUEFIRSTFREE -> {
				if(!isClientInQueue(positions, client)) {
					mainService.enqueue(client.id!!, queueId)
				} else {
					sender.send(query.message.chatId, "Вы уже в очереди")
				}
			}

			Actions.ENQUEUEBYNUMBER -> return "enqueue-by-number"
		}
		return nextState
	}

	fun isClientInQueue(positions: List<Position>, client: Client): Boolean {
		for(position in positions)
			if(position.client == client)
				return true
		return false
	}
}
