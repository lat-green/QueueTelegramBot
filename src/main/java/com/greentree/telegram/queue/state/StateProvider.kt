package com.greentree.telegram.queue.state

interface StateProvider {

	fun findOrNull(chatId: Long, stateName: String): Response?

	sealed interface Response
}


