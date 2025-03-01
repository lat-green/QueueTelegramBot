package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StartRequestState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class BeginStateProvider : StateProvider {

	override fun findOrNull(sender: AbsSender, stateName: String): ChatState? {
		if(stateName != "begin") return null
		return StartRequestState("main-menu")
	}
}