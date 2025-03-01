package com.greentree.telegram.queue.state

import org.telegram.telegrambots.meta.bots.AbsSender

interface StateProvider {

	fun findOrNull(sender: AbsSender, stateName: String): ChatState?
}