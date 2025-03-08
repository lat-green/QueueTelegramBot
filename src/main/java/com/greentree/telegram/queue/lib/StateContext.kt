package com.greentree.telegram.queue.lib

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

interface StateContext {

	fun onCallback(onMessage: (CallbackQuery) -> Unit)
	fun onMessage(onMessage: (Message) -> Unit)
}