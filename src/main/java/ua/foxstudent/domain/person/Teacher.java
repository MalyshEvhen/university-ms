package ua.foxstudent.domain.person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.foxstudent.domain.schedule.Lesson;
import ua.foxstudent.domain.activity.Course;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Teacher extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AcademicDegree academicDegree;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProfessionalPost professionalPost;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "teachers_courses",
        joinColumns = @JoinColumn(
            name = "teacher_id",
            foreignKey = @ForeignKey(name = "fk_teachers_courses_teachers")),
        inverseJoinColumns = @JoinColumn(
            name = "course_id",
            foreignKey = @ForeignKey(name = "fk_teachers_courses_courses")),
        indexes = {
            @Index(name = "idx_teachers_courses_teacher_id", columnList = "teacher_id"),
            @Index(name = "idx_teachers_courses_course_id", columnList = "course_id")})
    private Set<Course> courses = new HashSet<>();

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    public void addCourses(Set<Course> courses) {
        this.courses.addAll(courses);
        courses.forEach(c -> c.addTeacher(this));
    }

    public void removeCourses(Set<Course> courses) {
        this.courses.removeAll(courses);
        courses.forEach(c -> c.removeTeacher(this));
    }
}
