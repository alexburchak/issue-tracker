package org.alexburchak.issuetracker.data.domain.redis;

import lombok.Getter;
import lombok.Setter;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author alexburchak
 */
@RedisHash
@Getter
@Setter
public class Conversation {
    @Id
    private Long id;
    @Indexed
    private Long chatId;
    private State state;
    private Context context;
}
