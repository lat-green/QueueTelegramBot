package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.service.MainService
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueByNumberState(
	val mainService: MainService,
	val queueId: Long,
	val nextState: String
) : ChatState {

	override fun init(sender: ChatSender) {
		if(mainService.isClientInQueue(sender.chatId, queueId)) {
			sender.send("Вы уже в очереди")
			redirect("main-menu")
		}
		sender.send("Введите желаемый номер")
	}

	override fun onMessage(sender: AbsSender, message: Message): Nothing {
		val text = message.text
		val number = text.toInt()
		if (number <= 0){
			sender.send(message.chatId, "Ты дурочок или что?")
			redirect("main-menu")
		}
		if (!mainService.enqueueByNumber(message.chatId, queueId, number))
			sender.send(message.chatId, "Место занято")
		redirect(nextState)
	}
}
