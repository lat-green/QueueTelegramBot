package com.greentree.telegram.queue.lib

data class RedirectException(
	val nextStateName: String,
) : RuntimeException(nextStateName, null, false, false)

fun redirect(nextStateName: String?): Nothing = throw RedirectException(nextStateName!!)

data object ReInitializeException : RuntimeException(null, null, false, false) {

	private fun readResolve(): Any = ReInitializeException
}

fun reInitialize(): Nothing = throw ReInitializeException

data object NothingException : RuntimeException(null, null, false, false) {

	private fun readResolve(): Any = ReInitializeException
}

fun nothing(): Nothing = throw NothingException