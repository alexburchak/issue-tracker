package org.alexburchak.issuetracker.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.alexburchak.issuetracker.bot.handler.action.Action;
import org.alexburchak.issuetracker.bot.handler.action.ActionFactory;
import org.alexburchak.issuetracker.data.domain.redis.Conversation;
import org.alexburchak.issuetracker.data.repository.redis.ConversationRepository;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author alexburchak
 */
public class UpdateHandlerTest {
    private static final long CHAT_ID = 1L;

    @Spy
    private TelegramBot telegramBot = new TelegramBot("123");
    @Mock
    private ActionFactory actionFactory;
    @Mock
    private ConversationRepository conversationRepository;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @InjectMocks
    private UpdateHandler updateHandler = new UpdateHandler();

    private Conversation conversation;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        conversation = new Conversation();
        conversation.setChatId(CHAT_ID);
        conversation.setState(State.STARTED);
        doReturn(conversation).when(conversationRepository).findByChatId(CHAT_ID);
        doReturn(conversation).when(conversationRepository).save(any(Conversation.class));

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        reset(telegramBot, update, message, chat);
    }

    @Test
    public void testHandleUpdateSuccess() {
        doReturn(new Action() {
            @Override
            public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
                return this;
            }
        }).when(actionFactory).getAction(conversation.getState());

        updateHandler.handleUpdate(update);

        verify(conversationRepository).save(any(Conversation.class));
        verifyNoMoreInteractions(telegramBot);
    }

    @Test
    public void testHandleUpdateFailure() {
        doReturn(new Action() {
            @Override
            public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
                throw new RuntimeException();
            }
        }).when(actionFactory).getAction(conversation.getState());

        updateHandler.handleUpdate(update);

        verifyNoMoreInteractions(telegramBot);
    }
}
