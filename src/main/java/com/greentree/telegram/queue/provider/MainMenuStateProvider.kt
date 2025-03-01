package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.bot.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import org.springframework.stereotype.Component

@Component
class MainMenuStateProvider : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "main-menu") return null


		return ChooseState("Меню", mapOf("Очереди" to "choosing-queue"))
	}
}