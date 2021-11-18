package com.example.project.database.entity;

import com.example.project.database.enums.AreaOfInterest;
import com.example.project.database.enums.Status;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private String description;

    @Column
    private String notes;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private AreaOfInterest areaOfInterest;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @OneToMany(mappedBy = "requestEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @ToString.Exclude
    private List<ResourceEntity> resourceEntities;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "users")
    @ToString.Exclude
    private User user;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RequestEntity that = (RequestEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }


}
