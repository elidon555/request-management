package com.example.project.database.entity;

import com.example.project.database.enums.Seniority;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "resource_skills",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "skill"))
    @ToString.Exclude
    private List<SkillEntity> skillEntities;

    private String resourceNotes;

    @Enumerated(EnumType.STRING)
    private Seniority seniority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request")
    @ToString.Exclude
    private RequestEntity requestEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourceEntity that = (ResourceEntity) o;
        return Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
