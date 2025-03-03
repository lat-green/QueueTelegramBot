package com.greentree.telegram.queue.state

data class Redirect(
	val nextStateName: String,
) : StateProvider.Response

