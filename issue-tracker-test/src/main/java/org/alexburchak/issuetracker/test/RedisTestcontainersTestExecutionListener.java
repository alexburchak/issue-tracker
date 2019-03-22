package org.alexburchak.issuetracker.test;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.GenericContainer;

/**
 * @author alexburchak
 */
public class RedisTestcontainersTestExecutionListener implements TestExecutionListener {
    private static final int REDIS_PORT = 6379;

    private GenericContainer redis;

    @Override
    public void beforeTestClass(TestContext testContext) {
        redis = new GenericContainer("redis:latest")
                .withExposedPorts(REDIS_PORT);
        redis.start();

        System.setProperty("spring.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));
        // fail fast on Redis container shutdown
        System.setProperty("spring.redis.timeout", "1000");
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        redis.stop();
    }
}
