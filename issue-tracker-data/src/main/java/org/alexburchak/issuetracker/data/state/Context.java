package org.alexburchak.issuetracker.data.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author alexburchak
 */
@JsonSubTypes({
        @JsonSubTypes.Type(name = "issue", value = IssueContext.class),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class Context {
}
