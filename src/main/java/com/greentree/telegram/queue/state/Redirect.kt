package com.greentree.telegram.queue.state

data class Redirect(
	val nextStateName: String,
) : RuntimeException(nextStateName, null, false, false)

fun redirect(nextStateName: String): Nothing = throw Redirect(nextStateName)