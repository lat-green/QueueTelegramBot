package com.greentree.telegram.queue.state

data class Redirect(
	val nextStateName: String,
) : StateProvider.Response

data class RedirectException(
	val nextStateName: String,
) : RuntimeException(nextStateName, null, false, false)

fun redirect(nextStateName: String) = Redirect(nextStateName)