package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.*
import org.springframework.stereotype.Component

@Component
class MainMenuStateProvider (val mainService: MainService) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "main-menu") return null

		val client = mainService.findClientByChatId(sender.chatId)

		var buttons = mutableMapOf("Очереди" to "choosing-queue", "Опции" to "options")
		if (client.isAdmin){
			buttons.put("Создание очереди", "create-queue")
			buttons.put("Удаление очереди", "delete-queue")
		}
		return ChooseState("Меню", buttons)
	}
}