package ua.foxstudent.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "classrooms",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_classrooms_classroom_number",
        columnNames = "classroom_number"))
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @NotNull
    @NaturalId
    @Column(name = "classroom_number", nullable = false, unique = true)
    private Integer number;

    @Column(name = "classroom_description")
    private String description;

    @Positive
    @NotNull
    @Column(nullable = false)
    private Integer capacity;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "classroom")
    private List<Lesson> lessons = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @CreationTimestamp
    private LocalDateTime createAt;

    public Classroom(@Positive @NotNull Integer number,
                     @Positive @NotNull Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Classroom classroom = (Classroom) o;

        if (id == null ||
            classroom.id == null ||
            !Objects.equals(id, classroom.id)) return false;
        if (!number.equals(classroom.number)) return false;
        if (!Objects.equals(description, classroom.description))
            return false;
        return capacity.equals(classroom.capacity);
    }

    @Override
    public int hashCode() {
        return number.hashCode() + capacity.hashCode();
    }
}
