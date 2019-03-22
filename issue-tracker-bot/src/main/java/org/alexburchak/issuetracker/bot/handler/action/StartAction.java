package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.STARTED)
public class StartAction extends Action<Context> {
    @Override
    public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
        bot.execute(new SendMessage(chat.id(), "Welcome!"));

        return action(State.AUTHENTICATION)
                .context(null);
    }
}
