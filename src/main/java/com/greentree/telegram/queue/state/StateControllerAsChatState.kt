package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.initialize
import com.greentree.telegram.queue.lib.nothing
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import java.lang.Thread.*

data class StateControllerAsChatState(
	val origin: StateController,
) : ChatState {

	private lateinit var onCallbackFun: (CallbackQuery) -> Unit
	private lateinit var onMessageFun: (Message) -> Unit

	override fun init(sender: ChatSender): Nothing {
		val (sender, chatId) = sender
		val context = object : StateController.Context {
			override val chatId: Long
				get() = chatId
			private var lastMessage: Message? = null
			private var isInitializing = true

			override fun execute(sendMessage: SendMessage): Message {
				val message = sender.execute(sendMessage)
				if(isInitializing) {
					lastMessage = message
				}
				return message
			}

			override fun onCallback(onCallback: (String) -> Nothing): Nothing {
				isInitializing = false
				val lastMessage = lastMessage ?: TODO("call execute(SendMessage) before onCallback")
				this@StateControllerAsChatState.onCallbackFun = { callbackQuery ->
					if(callbackQuery.message.messageId == lastMessage.messageId) {
						onCallback(callbackQuery.data)
					}
					text("Была замечена подозрительная активность. Запуск самоуничтожения")
					repeat(3) {
						text((3 - it).toString())
						sleep(1000)
					}
					text("BOOM!!!")
					redirect("begin")
				}
				nothing()
			}

			override fun onMessage(onMessage: (Message) -> Nothing): Nothing {
				isInitializing = false
				this@StateControllerAsChatState.onMessageFun = onMessage
				nothing()
			}
		}
		origin.initialize(context)
	}

	override fun onMessage(sender: AbsSender, message: Message) {
		if(!::onMessageFun.isInitialized) {
			return
		}

		onMessageFun(message)
	}

	override fun onCallback(sender: AbsSender, query: CallbackQuery) {
		if(!::onCallbackFun.isInitialized) {
			return
		}

		onCallbackFun(query)
	}
}

fun StateController.asChatState() = StateControllerAsChatState(this)
