package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.data.repository.jpa.IssueRepository;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.LISTING_ISSUES)
public class ListIssuesAction extends Action<Context> {
    @Autowired
    private IssueRepository issueRepository;

    @Override
    public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
        bot.execute(new SendMessage(chat.id(), "Here is a list of all issues registered:")
                .disableNotification(true)
        );

        issueRepository.findAll().forEach(
                i -> {
                    bot.execute(new SendMessage(chat.id(), String.format("<b>%s</b>\n<i>%s</i>", i.getName(), i.getDescription()))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(true)
                            .disableNotification(true)
                    );
                }
        );

        return action(State.MENU)
                .context(null);
    }
}
