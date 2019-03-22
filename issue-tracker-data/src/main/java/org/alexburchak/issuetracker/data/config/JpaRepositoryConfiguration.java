package org.alexburchak.issuetracker.data.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author alexburchak
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.alexburchak.issuetracker.data.repository.jpa")
@EntityScan(basePackages = "org.alexburchak.issuetracker.data.domain.jpa")
public class JpaRepositoryConfiguration {
}
