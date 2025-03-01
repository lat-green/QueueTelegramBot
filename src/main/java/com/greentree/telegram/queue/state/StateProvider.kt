package com.greentree.telegram.queue.state

interface StateProvider {

	fun findOrNull(sender: ChatSender, stateName: String): Response?

	sealed interface Response
}

