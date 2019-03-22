package org.alexburchak.issuetracker.data.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author alexburchak
 */
@Entity
@Table(name = "Issue")
@Getter
@Setter
public class Issue {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "description", length = 2048, nullable = false)
    private String description;
    @OneToMany(targetEntity = Photo.class, mappedBy = "issue", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Photo> photos = new HashSet<>();
}
