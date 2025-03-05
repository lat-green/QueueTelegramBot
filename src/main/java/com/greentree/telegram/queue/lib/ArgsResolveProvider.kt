package com.greentree.telegram.queue.lib

data class ArgsResolveProvider(
	val origin: StateController.Provider,
) : StateController.Provider {

	override fun provide(name: String): StateController? {
		val list = name.split("?")
		val origin = origin.provide(list[0]) ?: return null
		if(list.size != 2)
			return origin
		val params = mutableMapOf<String, String>()
		val paramsPart = list[1].split("&")
		for(paramPair in paramsPart) {
			val (key, value) = paramPair.split("=")
			params[key] = value
		}
		return AddParamsStateController(origin, params)
	}
}