package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.ChooseState
import com.greentree.telegram.queue.state.StartRequestState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class MainMenuStateProvider : StateProvider {

	override fun findOrNull(stateName: String): ChatState? {
		if(stateName != "main-menu") return null


		return ChooseState("Меню", mapOf("Очереди" to "choosing-queue"))
	}
}