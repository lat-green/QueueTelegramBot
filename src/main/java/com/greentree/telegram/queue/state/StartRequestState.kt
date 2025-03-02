package com.greentree.telegram.queue.state

import com.greentree.telegram.queue.model.Client
import com.greentree.telegram.queue.repository.ClientRepository
import lombok.extern.slf4j.Slf4j
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Slf4j
data class StartRequestState(val repository: ClientRepository, val nextState: String) : ChatState {

	override fun onMessage(sender: AbsSender, message: Message): String? {
		val text = message.text
		if("/start" == text){
			if (repository.findByChatId(message.chatId).isEmpty){
				val client = Client()
				client.chatId = message.chatId
				client.name = "Anonimus"
				repository.save(client)
			}
			return nextState
		}
		return null
	}

	override fun init(sender: ChatSender) {
		sender.send("Введите /start")
	}
}
