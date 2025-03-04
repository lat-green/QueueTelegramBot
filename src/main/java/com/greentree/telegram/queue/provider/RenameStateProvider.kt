package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.RenameState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class RenameStateProvider(val mainService: MainService) : StateProvider {

	override fun findOrNull(chatId: Long, stateName: String): ChatState? {
		if(stateName != "rename") return null

		return RenameState(mainService, "main-menu")
	}
}