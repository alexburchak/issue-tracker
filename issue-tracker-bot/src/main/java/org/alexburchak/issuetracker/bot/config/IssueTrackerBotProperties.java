package org.alexburchak.issuetracker.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author alexburchak
 */
@Component
@ConfigurationProperties("issue-tracker-bot")
@Getter
@Setter
public class IssueTrackerBotProperties {
    private String apiKey;
}
