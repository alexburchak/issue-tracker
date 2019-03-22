package org.alexburchak.issuetracker.data.repository.jpa;

import org.alexburchak.issuetracker.data.domain.jpa.Issue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author alexburchak
 */
@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {
}
