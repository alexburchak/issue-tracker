package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.IssueContext;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.ASKING_ISSUE_NAME)
public class AskIssueNameAction extends Action<IssueContext> {
    @Override
    public Action<? extends Context>  prompt(TelegramBot bot, Chat chat) {
        bot.execute(new SendMessage(chat.id(), "Please name your issue"));

        return this;
    }

    @Override
    public Action<? extends Context>  command(TelegramBot bot, Update update) {
        Message message = update.message();

        String name = message.text();
        if (name != null) {
            return action(State.ASKING_ISSUE_DESCRIPTION)
                    .context(IssueContext.builder()
                            .name(name)
                            .build()
                    );
        }

        return this;
    }
}
