package com.greentree.telegram.queue.lib

data class MapProvider(
	val map: Map<String, StateController>,
) : StateController.Provider {

	constructor(vararg pairs: Pair<String, StateController>) : this(mapOf(*pairs))

	override fun provide(name: String): StateController? = map[name]
}