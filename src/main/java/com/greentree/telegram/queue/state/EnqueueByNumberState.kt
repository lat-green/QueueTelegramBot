package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.model.Position
import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.repository.PositionRepository
import com.greentree.telegram.queue.repository.QueueRepository
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueByNumberState(
	val positionRepository: PositionRepository,
	val queueRepository: QueueRepository,
	val clientRepository: ClientRepository,
	val mainService: MainService,
	val queueId: Long,
	val clientId: Long,
	val nextState: String
) : ChatState {

	override fun init(sender: ChatSender) {
		if (mainService.isClientInQueue(sender.chatId, queueId)){
			sender.send("Вы уже в очереди")
		}
			sender.send("Введите желаемый номер позиции")
	}

	override fun onMessage(sender: AbsSender, message: Message): String? {
		val text = message.text
		val number = text.toInt() - 1
		val output = mainService.enqueueByNumber(message.chatId, queueId, number)
		if (!output.left)
			sender.send(message.chatId, output.right)
		return nextState
	}
}
