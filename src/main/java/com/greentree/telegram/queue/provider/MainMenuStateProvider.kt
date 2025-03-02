package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrElse

@Component
class MainMenuStateProvider (val repository: ClientRepository) : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "main-menu") return null

		val client = repository.findByChatId(sender.chatId).getOrElse { TODO() }

		var buttons = mutableMapOf("Очереди" to "choosing-queue")
		if (client.isAdmin){
			buttons.put("Создание очереди", "create-queue")
			buttons.put("Удаление очереди", "delete-queue")
		}
		return ChooseState("Меню", buttons)
	}
}