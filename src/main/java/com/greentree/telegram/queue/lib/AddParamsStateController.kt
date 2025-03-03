package com.greentree.telegram.queue.lib

data class AddParamsStateController(
	val origin: StateController,
	val params: Map<String, String>,
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>) =
		origin.initialize(this, params + this@AddParamsStateController.params)
}
