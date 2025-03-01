package com.greentree.telegram.queue.state

interface StateProvider {

	fun findOrNull(stateName: String): ChatState?
}