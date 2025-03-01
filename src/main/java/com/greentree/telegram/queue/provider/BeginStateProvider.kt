package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StartRequestState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class BeginStateProvider : StateProvider {

	override fun findOrNull(stateName: String): ChatState? {
		if(stateName != "begin") return null
		return StartRequestState("main-menu")
	}
}