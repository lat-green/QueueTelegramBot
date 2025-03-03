package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.controller.BeginStateController
import com.greentree.telegram.queue.controller.QueueStateController
import com.greentree.telegram.queue.lib.ArgsResolveProvider
import com.greentree.telegram.queue.lib.MapProvider
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.ChatState
import com.greentree.telegram.queue.state.StateProvider
import com.greentree.telegram.queue.state.asChatState
import org.springframework.stereotype.Component

@Component
class StateControllerProvider(
	val service: MainService,
) : StateProvider {

	private val provider = ArgsResolveProvider(
		MapProvider(
			"begin" to BeginStateController("queue"),
			"queue" to QueueStateController(service)
		)
	)

	override fun findOrNull(chatId: Long, stateName: String): ChatState? = provider.provide(stateName)?.asChatState()
}