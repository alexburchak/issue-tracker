package org.alexburchak.issuetracker.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author alexburchak
 */
@Configuration
@EnableRedisRepositories(basePackages = "org.alexburchak.issuetracker.data.repository.redis")
public class RedisRepositoryConfiguration {
}
