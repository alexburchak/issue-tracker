package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.AUTHENTICATION)
public class AuthenticateAction extends Action<Context> {
    @Override
    public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
        KeyboardButton button = new KeyboardButton("Send phone number")
                .requestContact(true);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(new KeyboardButton[]{button})
                .resizeKeyboard(true);

        bot.execute(new SendMessage(chat.id(), "Please provide your phone number")
                .replyMarkup(keyboard)
        );

        return this;
    }

    @Override
    public Action<? extends Context> command(TelegramBot bot, Update update) {
        Message message = update.message();

        Contact contact = message.contact();
        if (contact != null
                && contact.phoneNumber() != null
                && Objects.equals(message.from().id(), contact.userId())) {

            bot.execute(new SendMessage(message.chat().id(), "Thank you")
                    .replyMarkup(new ReplyKeyboardRemove())
            );

            return action(State.MENU)
                    .context(null);
        }

        bot.execute(new SendMessage(message.chat().id(), "The phone number is incorrect"));

        return this;
    }
}
