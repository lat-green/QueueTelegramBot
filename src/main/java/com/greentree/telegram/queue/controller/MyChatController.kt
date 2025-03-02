package com.greentree.telegram.queue.controller

import com.greentree.commons.util.react.useRef
import com.greentree.telegram.queue.lib.StateContext
import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.redirect
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class MyChatController(
	val service: MainService,
) {

	@StateController
	fun StateContext.begin(message: Message?): String {
		message?.on {
			if(text == "/start")
				redirect("counter?id=15")
		}
		return "Ведите /start"
	}

	@StateController
	fun StateContext.counter(message: Message?, @Qualifier("type") type: String, @Qualifier("id") start: Int): Any {
		var ref by useRef(start)
		message?.on {
			if(text == "/inc") {
				ref++
				return "[$type] ($ref) ++"
			}
		}
		return "[$type] ($ref) Ведите /inc"
	}
}

inline fun <T> T.on(block: T.() -> Unit) {
	block()
}