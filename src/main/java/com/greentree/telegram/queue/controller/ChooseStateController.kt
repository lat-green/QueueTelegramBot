package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect

class ChooseStateController(
	val text: String,
	val nextStates: Map<String, String>
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		executeInlineKeyboard(text, nextStates.keys)

		onCallback {
			redirect(nextStates[it]!!)
		}
	}
}

fun StateController.Context.choose(
	text: String,
	nextStates: Map<String, String>,
): Nothing = inline(ChooseStateController(text, nextStates))

fun StateController.Context.sortedChoose(
	text: String,
	nextStates: Map<String, String>,
): Nothing = inline(ChooseStateController(text, nextStates.toSortedMap()))