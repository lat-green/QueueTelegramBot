package com.greentree.telegram.queue

import com.greentree.telegram.queue.state.ChatSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun createInlineKeyboard(text: String, nextStates: Map<String, String?>, sender: ChatSender){
    val keyboard = InlineKeyboardMarkup()
    val first_row: MutableList<InlineKeyboardButton> = ArrayList()
    val buttons = java.util.List.of<List<InlineKeyboardButton>>(first_row)
    for(name in nextStates.keys) {
        val button = InlineKeyboardButton()
        button.text = name
        button.callbackData = name
        first_row.add(button)
    }
    keyboard.keyboard = buttons
    val send = SendMessage(sender.chatId.toString(), text)
    send.replyMarkup = keyboard
    sender.sender.execute(send)
}