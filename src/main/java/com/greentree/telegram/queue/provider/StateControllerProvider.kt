package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.controller.*
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
			"begin" to BeginStateController("main-menu", service),
			"main-menu" to MainMenuStateController(service),
			"choose-queue" to ChooseQueueStateController(service),
			"queue" to QueueStateController(service),
			"create-queue" to CreateQueueStateController("main-menu", service),
			"delete-queue" to DeleteQueueStateController("main-menu", service),
			"enqueue-first-free" to EnqueueFirstFreeStateController("main-menu", service),
			"enqueue-by-number" to EnqueueByNumberStateController("main-menu", service),
			"enqueue-deadline" to EnqueueDeadlineStateController("main-menu", service),
			"dequeue" to DequeueStateController("main-menu", service),
			"options" to OptionsStateController(),
			"rename" to RenameStateController("main-menu", service),
		)
	)

	override fun findOrNull(chatId: Long, stateName: String): ChatState? = provider.provide(stateName)?.asChatState()
}