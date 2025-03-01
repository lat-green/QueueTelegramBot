package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.bot.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StartRequestState
import org.springframework.stereotype.Component

@Component
class BeginStateProvider : StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "begin") return null
		return StartRequestState("main-menu")
	}
}