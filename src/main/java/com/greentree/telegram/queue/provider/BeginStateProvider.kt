package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StartRequireState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class BeginStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(stateName != "begin") return null

		return StartRequireState(mainService, "main-menu")
	}
}