package com.greentree.example.telegram;

import com.greentree.example.telegram.state.ChatState;
import com.greentree.example.telegram.state.EndState;
import com.greentree.example.telegram.state.PreStartState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final Map<Long, ChatState> states = new HashMap<>();

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                var message = update.getMessage();
                onMessage(message);
            }
            if (update.hasCallbackQuery()) {
                var query = update.getCallbackQuery();
                onCallback(query);
            }
        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }

    private void onMessage(Message message) throws TelegramApiException {
        long chat_id = message.getChatId();
        var state = states.remove(chat_id);
        if (state == null)
            state = beginState();
        var next_state = state.onMessage(this, message);
        if (next_state == null)
            next_state = beginState();
        states.put(chat_id, next_state);
        next_state.init(this, chat_id);
    }

    private void onCallback(CallbackQuery query) throws TelegramApiException {
        long chat_id = query.getMessage().getChatId();
        var state = states.remove(chat_id);
        if (state == null)
            state = beginState();
        var next_state = state.onCallback(this, query);
        if (next_state == null)
            next_state = beginState();
        states.put(chat_id, next_state);
        next_state.init(this, chat_id);
    }

    private ChatState beginState() {
        return new PreStartState(new EndState());
    }

}
