package org.alexburchak.issuetracker.bot;

import org.alexburchak.issuetracker.data.domain.jpa.Issue;
import org.alexburchak.issuetracker.data.domain.redis.Conversation;
import org.alexburchak.issuetracker.data.repository.jpa.IssueRepository;
import org.alexburchak.issuetracker.data.repository.redis.ConversationRepository;
import org.alexburchak.issuetracker.data.state.IssueContext;
import org.alexburchak.issuetracker.data.state.State;
import org.alexburchak.issuetracker.test.MySQLTestcontainersTestExecutionListener;
import org.alexburchak.issuetracker.test.RedisTestcontainersTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author alexburchak
 */
@SpringBootTest(classes = IssueTrackerBotApplication.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = {
        RedisTestcontainersTestExecutionListener.class,
        MySQLTestcontainersTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
}, inheritListeners = false)
public class IssueTrackerBotApplicationTest extends AbstractTestNGSpringContextTests {
    private static final Long CHAT_ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    public void testIssueRepository() {
        Issue issue = new Issue();
        issue.setName(NAME);
        issue.setDescription(DESCRIPTION);
        issueRepository.save(issue);

        List<Issue> issues = new ArrayList<>();
        issueRepository.findAll()
                .forEach(issues::add);
        assertEquals(issues.size(), 1);

        issue = issues.get(0);
        assertEquals(issue.getName(), NAME);
        assertEquals(issue.getDescription(), DESCRIPTION);
        assertTrue(issue.getPhotos().isEmpty());
    }

    @Test
    public void testConversationRepository() {
        Conversation conversation = conversationRepository.findByChatId(CHAT_ID);
        assertNull(conversation);

        conversation = new Conversation();
        conversation.setChatId(CHAT_ID);
        conversation.setContext(IssueContext.builder().name(NAME).description(DESCRIPTION).build());
        conversation.setState(State.ASKING_ISSUE_PHOTO);
        conversationRepository.save(conversation);

        conversation = conversationRepository.findByChatId(CHAT_ID);
        assertNotNull(conversation);
        assertEquals(conversation.getChatId(), CHAT_ID);
        assertEquals(conversation.getState(), State.ASKING_ISSUE_PHOTO);
        assertTrue(conversation.getContext() instanceof IssueContext);
    }
}
