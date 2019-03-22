package org.alexburchak.issuetracker.data.repository.redis;

import org.alexburchak.issuetracker.data.domain.redis.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author alexburchak
 */
@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {
    Conversation findByChatId(Long chatId);
}
