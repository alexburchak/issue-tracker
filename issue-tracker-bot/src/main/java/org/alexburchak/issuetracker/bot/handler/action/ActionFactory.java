package org.alexburchak.issuetracker.bot.handler.action;

import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author alexburchak
 */
@Component
public class ActionFactory {
    @Autowired
    private ApplicationContext applicationContext;

    private Map<State, Class<?>> actionClass;

    @PostConstruct
    public void postConstruct() {
        actionClass = applicationContext.getBeansWithAnnotation(ActionBinding.class).values().stream()
                .collect(Collectors.toMap(a -> ((Action) a).getState(), Object::getClass));
    }

    public <T extends Context> Action<T> getAction(State state) {
        return (Action<T>) applicationContext.getBean(actionClass.get(state));
    }
}
