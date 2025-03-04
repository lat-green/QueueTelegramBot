package com.greentree.telegram.queue.lib

data class MultiProvider(
	val providers: Sequence<StateController.Provider>,
) : StateController.Provider {

	override fun provide(name: String): StateController? = providers
		.mapNotNull { it.provide(name) }
		.firstOrNull()
}