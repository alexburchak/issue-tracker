package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author alexburchak
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class Action<T extends Context> implements Comparable<Action> {
    @Autowired
    private ActionFactory actionFactory;

    @Getter
    private State state;
    private T context;

    @PostConstruct
    public void postConstruct() {
        state = Optional.ofNullable(getClass().getAnnotation(ActionBinding.class))
                .orElseThrow(IllegalStateException::new)
                .value();
    }

    /**
     * Prompt for something, e.g. ask for text, photo etc
     *
     * @param bot bot
     * @param chat chat
     * @return new action
     */
    public abstract Action<? extends Context>  prompt(TelegramBot bot, Chat chat);

    /**
     * Handle a command, e.g. text was sent
     *
     * @param bot bot
     * @param update update data
     * @return new action
     */
    public Action<? extends Context> command(TelegramBot bot, Update update) {
        return prompt(bot, update.message().chat());
    }

    /**
     * Callback method for buttons
     *
     * @param bot bot
     * @param callbackQuery callback data
     * @return new action
     */
    public Action<? extends Context> callback(TelegramBot bot, CallbackQuery callbackQuery) {
        return this;
    }

    public <R extends Context> Action<R> action(State state) {
        return actionFactory.getAction(state);
    }

    public T context() {
        return context;
    }

    public Action<T> context(T context) {
        this.context = context;
        return this;
    }

    @Override
    public int compareTo(Action other) {
        return getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
