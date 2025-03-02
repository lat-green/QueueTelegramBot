package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.entity.Position
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueByNumberState(
	val positionRepository: PositionRepository,
	val queueRepository: QueueRepository,
	val clientRepository: ClientRepository,
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	override fun onCallback(sender: AbsSender, query: CallbackQuery): String {
		val text = query.data
		val client = clientRepository.findByChatId(query.message.chatId).orElseThrow()
		val number = text.toInt()
		val output = mainService.enqueueByNumber(query.message.chatId, queueId, number)
		if (!output.left)
			sender.send(query.message.chatId, output.right)
		return nextState
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите желаемый номер позиции")
	}

}
