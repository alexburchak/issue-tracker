package org.alexburchak.issuetracker.bot;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author alexburchak
 */
@SpringBootApplication(scanBasePackages = "org.alexburchak.issuetracker")
@EnableConfigurationProperties
@ComponentScan(basePackages = "org.alexburchak.issuetracker")
public class IssueTrackerBotApplication {
    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder())
                .addCommandLineProperties(true)
                .run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(IssueTrackerBotApplication.class)
                .bannerMode(Banner.Mode.LOG);
    }
}
