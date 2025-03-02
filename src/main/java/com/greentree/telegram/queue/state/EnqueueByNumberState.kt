package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.entity.Position
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueByNumberState(
	val positionRepository: PositionRepository,
	val queueRepository: QueueRepository,
	val clientRepository: ClientRepository,
	val queueId: Long,
	val nextState: String
) : ChatState {

	val queue = queueRepository.findById(queueId).orElseThrow()
	val positions = positionRepository.findAllByQueue(queue)

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val text = query.data
		val client = clientRepository.findByChatId(query.message.chatId).orElseThrow()
		val number = text.toInt()
		if(isNumberFree(positions, number)) {
			val position = Position()
			position.number = number
			position.queue = queue
			position.client = client
		} else {
			sender.send(query.message.chatId, "Место уже занято")
		}
		return nextState
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите желаемый номер позиции")
	}

	fun isNumberFree(positions: List<Position>, number: Int): Boolean {
		for(position in positions)
			if(position.number == number)
				return false
		return true
	}
}
