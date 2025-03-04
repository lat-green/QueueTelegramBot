package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.initialize
import com.greentree.telegram.queue.lib.nothing
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

data class StateControllerAsChatState(
	val origin: StateController,
) : ChatState {

	private lateinit var onCallback: (String) -> Unit
	private lateinit var onMessage: (Message) -> Unit

	override fun init(sender: ChatSender): Nothing {
		val (sender, chatId) = sender
		val context = object : StateController.Context {
			override val chatId: Long
				get() = chatId

			override fun execute(sendMessage: SendMessage): Message = sender.execute(sendMessage)

			override fun onCallback(onCallback: (String) -> Nothing): Nothing {
				this@StateControllerAsChatState.onCallback = onCallback
				nothing()
			}

			override fun onMessage(onMessage: (Message) -> Nothing): Nothing {
				this@StateControllerAsChatState.onMessage = onMessage
				nothing()
			}
		}
		origin.initialize(context)
	}

	override fun onMessage(sender: AbsSender, message: Message) {
		onMessage(message)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery) {
		onCallback(query.data)
	}
}

fun StateController.asChatState() = StateControllerAsChatState(this)
