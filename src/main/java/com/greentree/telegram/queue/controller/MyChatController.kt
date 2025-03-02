package com.greentree.telegram.queue.controller

import com.greentree.telegram.queue.lib.StateController
import com.greentree.telegram.queue.service.MainService
import com.greentree.telegram.queue.state.redirect
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class MyChatController(
	val service: MainService,
) {

	@StateController
	fun begin(message: Message?): String {
		message?.on {
			if(text == "/start")
				redirect("counter?id=15")
		}
		return "Ведите /start"
	}

	@StateController
	fun counter(
		message: Message?,
		query: CallbackQuery?,
		@Qualifier("type") type: String,
		@Qualifier("id") start: Int
	): Any {
		message?.on {
			if(text == "/inc") {
				return "[$type] ++"
			}
		}
		query?.on {
		}
		return "[$type] Ведите /inc"
	}
//	@StateController
//	fun choose(query: CallbackQuery?, sender: ChatSender){
//		query?.on {
//			val text = query.data
//			if(queues.containsKey(text))
//				return nextStates[text]
//			sender.send(query.message.chatId, "Выберите одно из представленных действий")
//			return null
//
//		}
//
//		val queues = mutableMapOf<String, String?>()
//		for(queue in service.findAllQueue())
//			queues[queue.name] = "queue?id=" + queue.id
//
//		if (queues.isEmpty()){
//			sender.send("Очередей нет")
//			redirect("main-menu")
//		}
//
//		createInlineKeyboard("Выберите предмет", queues.keys, sender)
//	}
}

inline fun <T> T.on(block: T.() -> Unit) {
	block()
}