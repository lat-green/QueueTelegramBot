package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class MainMenuStateProvider : StateProvider {

	override fun findOrNull(sender: AbsSender, stateName: String): ChatState? {
		if(stateName != "main-menu") return null


		return ChooseState("Меню", mapOf("Очереди" to "choosing-queue"))
	}
}