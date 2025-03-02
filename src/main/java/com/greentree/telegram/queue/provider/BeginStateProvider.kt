package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.repository.ClientRepository
import com.greentree.telegram.queue.state.ChatSender
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StartRequestState
import com.greentree.telegram.queue.state.StateProvider
import org.springframework.stereotype.Component

@Component
class BeginStateProvider (val repository: ClientRepository): StateProvider {

	override fun findOrNull(sender: ChatSender, stateName: String): ChatState? {
		if(stateName != "begin") return null

		return StartRequestState(repository, "main-menu")
	}
}