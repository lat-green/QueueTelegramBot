package com.greentree.telegram.queue.lib

import com.greentree.commons.util.react.ReactContext
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

interface StateContext : ReactContext {

	fun onCallback(onMessage: (CallbackQuery) -> Unit)
	fun onMessage(onMessage: (Message) -> Unit)
}