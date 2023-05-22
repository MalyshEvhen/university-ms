package ua.foxstudent.domain.activity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ua.foxstudent.domain.person.Student;
import ua.foxstudent.domain.schedule.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "academic_groups",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_academic_groups_name",
        columnNames = "group_name"))
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Name is required")
    @Column(name = "group_name", nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(
        mappedBy = "group",
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Student> students = new ArrayList<>();

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(
        mappedBy = "group",
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Lesson> lessons = new ArrayList<>();

    public Group(@NotNull @NotBlank String name) {
        this.name = name;
    }

    public Group(@NotNull Long id,
                 @NotNull @NotBlank String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return getId() != null && Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
