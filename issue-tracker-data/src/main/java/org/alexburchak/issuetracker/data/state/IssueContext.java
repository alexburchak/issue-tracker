package org.alexburchak.issuetracker.data.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author alexburchak
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class IssueContext extends Context {
    private String name;
    private String description;
    private List<String> photos;
}
