package org.alexburchak.issuetracker.test;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author alexburchak
 */
public class MySQLTestcontainersTestExecutionListener implements TestExecutionListener {
    private static final int MYSQL_PORT = 3306;
    private static final String MYSQL_DATABASE = "issues";
    private static final String MYSQL_USERNAME = "issue_tracker";
    private static final String MYSQL_PASSWORD = "bot";

    private MySQLContainer mysql;

    @Override
    public void beforeTestClass(TestContext testContext) {
        mysql = new MySQLContainer<>()
                .withDatabaseName(MYSQL_DATABASE)
                .withUsername(MYSQL_USERNAME)
                .withPassword(MYSQL_PASSWORD)
                .withExposedPorts(MYSQL_PORT);
        mysql.start();

        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL55Dialect");
        System.setProperty("spring.datasource.url", String.format("jdbc:mysql://localhost:%d/%s", mysql.getMappedPort(MYSQL_PORT), MYSQL_DATABASE));
        System.setProperty("spring.datasource.username", MYSQL_USERNAME);
        System.setProperty("spring.datasource.password", MYSQL_PASSWORD);
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        mysql.stop();
    }
}
