package ua.foxstudent.domain.activity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
import ua.foxstudent.domain.person.Teacher;
import ua.foxstudent.domain.schedule.Lesson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    name = "courses",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_courses_course_name",
        columnNames = "course_name"))
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Name is required")
    @Column(name = "course_name", nullable = false, unique = true)
    private String name;

    @Column(name = "course_description")
    private String description;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(
        mappedBy = "courses",
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Student> students = new HashSet<>();

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(
        mappedBy = "courses",
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Teacher> teachers = new HashSet<>();

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(
        mappedBy = "course",
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Lesson> lessons = new ArrayList<>();

    public Course(@NotNull @NotBlank String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(Long id,
                  @NotNull @NotBlank String name,
                  String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.removeAll(Set.of(student));
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.removeAll(Set.of(teacher));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course course = (Course) o;
        return getId() != null && Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

