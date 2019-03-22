package org.alexburchak.issuetracker.bot.handler.action;

import org.alexburchak.issuetracker.data.state.State;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alexburchak
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionBinding {
    State value();
}
