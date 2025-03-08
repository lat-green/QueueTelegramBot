package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.executeInlineKeyboard
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.lib.redirect
import com.greentree.telegram.queue.lib.text
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class ChooseStateController(
	val text: String,
	val nextStates: Map<String, String>
) : StateController {

	override fun StateController.Context.initialize(params: Map<String, String>): Nothing {
		executeInlineKeyboard(text, nextStates.keys)

		onCallback {
			redirect(nextStates[it] ?: run {
				text("Была замечена подозрительная активность. Запуск самоуничтожения")
				repeat(3){
					text((3 - it).toString())
					sleep(1000)
				}
				text("BOOM!!!")
				redirect("main-menu")
			})
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