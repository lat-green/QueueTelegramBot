package com.greentree.telegram.queue.lib

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

fun interface StateController {

	fun Context.initialize(params: Map<String, String>): Nothing

	interface Context {

		val chatId: Long

		fun execute(sendMessage: SendMessage): Message

		fun onCallback(onCallback: (String) -> Nothing): Nothing
		fun onMessage(onMessage: (Message) -> Nothing): Nothing

		fun inline(controller: StateController, params: Map<String, String> = mapOf()): Nothing {
			controller.initialize(this, params)
		}
	}

	interface Provider {

		fun provide(name: String): StateController?
	}
}

fun StateController.initialize(context: StateController.Context, params: Map<String, String> = mapOf()): Nothing =
	context.run {
		initialize(params)
	}

fun StateController.Context.text(text: String) = execute(SendMessage(chatId.toString(), text))