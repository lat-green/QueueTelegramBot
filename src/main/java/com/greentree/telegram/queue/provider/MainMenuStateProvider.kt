package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class MainMenuStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(stateName != "main-menu") return null
		val client = mainService.findClientByChatId(chatId)
		val buttons = mutableMapOf("Очереди" to "choosing-queue")
		if(client.isAdmin) {
			buttons.put("Создание очереди", "create-queue")
			buttons.put("Удаление очереди", "delete-queue")
		}
		return ChooseState("Главное меню", buttons)
	}
}