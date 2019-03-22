package org.alexburchak.issuetracker.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.alexburchak.issuetracker.bot.handler.action.Action;
import org.alexburchak.issuetracker.bot.handler.action.ActionFactory;
import org.alexburchak.issuetracker.data.domain.redis.Conversation;
import org.alexburchak.issuetracker.data.repository.redis.ConversationRepository;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Slf4j
@Component
public class UpdateHandler {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private ActionFactory actionFactory;
    @Autowired
    private ConversationRepository conversationRepository;

    public void handleUpdate(Update update) {
        try {
            Action action;
            Action newAction;
            Chat chat;
            Conversation conversation;

            CallbackQuery callbackQuery = update.callbackQuery();
            if (callbackQuery != null) {
                chat = callbackQuery.message().chat();
                conversation = getConversation(chat, callbackQuery.message());
                action = action(conversation);

                newAction = action.callback(telegramBot, callbackQuery);

                if (newAction.compareTo(action) == 0) {
                    newAction = newAction.prompt(telegramBot, chat);
                }
            } else {
                chat = update.message().chat();
                conversation = getConversation(chat, update.message());
                action = action(conversation);

                newAction = action.command(telegramBot, update);
            }

            while (newAction.compareTo(action) != 0) {
                action = newAction;
                newAction = action.prompt(telegramBot, chat);
            }

            saveConversation(conversation, newAction);
        } catch (Throwable t) {
            log.error("Exception processing update", t);
        }
    }

    private Action action(Conversation conversation) {
        return actionFactory.getAction(conversation.getState())
                .context(conversation.getContext());
    }

    private Conversation getConversation(Chat chat, Message message) {
        Conversation conversation = null;

        if (message == null || !"/start".equals(message.text())) {
            conversation = conversationRepository.findByChatId(chat.id());
        }

        if (conversation == null) {
            conversation = new Conversation();
            conversation.setChatId(chat.id());
            conversation.setState(State.STARTED);
            conversation.setContext(null);
        }
        return conversation;
    }

    private void saveConversation(Conversation conversation, Action action) {
        conversation.setState(action.getState());
        conversation.setContext(action.context());
        conversationRepository.save(conversation);
    }
}
