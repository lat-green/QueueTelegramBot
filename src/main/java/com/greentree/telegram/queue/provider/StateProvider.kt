package com.greentree.telegram.queue.provider

import com.greentree.telegram.queue.bot.ChatSender
import com.greentree.telegram.queue.state.ChatState

interface StateProvider {

	fun findOrNull(sender: ChatSender, stateName: String): ChatState?
}