package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.MENU)
public class ShowMenuAction extends Action<Context> {
    @Override
    public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
        InlineKeyboardButton newIssueButton = new InlineKeyboardButton("Create new issue")
                .callbackData(State.ASKING_ISSUE_NAME.name());
        InlineKeyboardButton listIssuesButton = new InlineKeyboardButton("List issues")
                .callbackData(State.LISTING_ISSUES.name());
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(new InlineKeyboardButton[]{newIssueButton, listIssuesButton});

        bot.execute(new SendMessage(chat.id(), "What would you like to do?")
                .replyMarkup(keyboard)
        );

        return this;
    }

    @Override
    public Action<? extends Context> callback(TelegramBot bot, CallbackQuery callbackQuery) {
        if (callbackQuery != null && callbackQuery.data() != null) {
            return action(State.valueOf(callbackQuery.data()))
                    .context(null);
        }

        return this;
    }
}
