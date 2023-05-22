package ua.foxstudent.domain.person;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.domain.activity.Group;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "students",
    indexes = @Index(
        name = "idx_students_group_id",
        columnList = "group_id"))
@Getter
@Setter
@ToString(exclude = "courses")
@NoArgsConstructor
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(
        name = "group_id",
        foreignKey = @ForeignKey(name = "fk_students_groups"))
    private Group group;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "students_courses",
        joinColumns = @JoinColumn(
            name = "student_id",
            foreignKey = @ForeignKey(name = "fk_students_courses_students")),
        inverseJoinColumns = @JoinColumn(
            name = "course_id",
            foreignKey = @ForeignKey(name = "fk_students_courses_courses")),
        indexes = {
            @Index(name = "idx_students_courses_student_id", columnList = "student_id"),
            @Index(name = "idx_students_courses_course_id", columnList = "course_id")})
    private Set<Course> courses = new HashSet<>();
    
    public void addCourses(Set<Course> courses) {
        this.courses.addAll(courses);
        courses.forEach(c -> c.addStudent(this));
    }
    
    public void removeCourses(Set<Course> courses) {
        this.courses.removeAll(courses);
        courses.forEach(c -> c.removeStudent(this));
    }
}
