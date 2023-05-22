package ua.foxstudent.domain.schedule;

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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "academic_hours",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_academic_hours_name", columnNames = "name")})
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AcademicHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "begin_at", nullable = false, unique = true)
    private LocalTime beginAt;

    @NotNull
    @Column(name = "end_at", nullable = false, unique = true)
    private LocalTime endAt;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "academicHour")
    private List<Lesson> lessons = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @CreationTimestamp
    private LocalDateTime createAt;

    public AcademicHour(@NotNull @NotBlank String name,
                        @NotNull LocalTime beginAt,
                        @NotNull LocalTime endAt) {
        this.name = name;
        this.beginAt = beginAt;
        this.endAt = endAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AcademicHour that = (AcademicHour) o;

        if ( id == null ||
            that.id == null ||
            !Objects.equals(id, that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!beginAt.equals(that.beginAt)) return false;
        return endAt.equals(that.endAt);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + beginAt.hashCode() + endAt.hashCode();
    }
}
