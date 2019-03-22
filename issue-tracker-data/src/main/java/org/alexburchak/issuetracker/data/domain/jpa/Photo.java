package org.alexburchak.issuetracker.data.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author alexburchak
 */
@Entity
@Table(name = "Photo")
@Getter
@Setter
public class Photo {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;
    @Column(name = "data", columnDefinition = "mediumblob", nullable = false)
    private byte data[];
    @ManyToOne(optional = false)
    @JoinColumn(name = "issue_id", nullable = false, foreignKey = @ForeignKey(name = "fk_photo_sssue"))
    private Issue issue;
}
