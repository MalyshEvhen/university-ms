package ua.foxstudent.domain.schedule;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.domain.person.Teacher;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(
    name = "lessons",
    indexes = {
        @Index(name = "idx_lessons_day_schedule_id", columnList = "day_schedule_id"),
        @Index(name = "idx_lessons_academic_hour_id", columnList = "academic_hour_id"),
        @Index(name = "idx_lessons_classroom_id", columnList = "classroom_id"),
        @Index(name = "idx_lessons_group_id", columnList = "group_id"),
        @Index(name = "idx_lessons_course_id", columnList = "course_id"),
        @Index(name = "idx_lessons_teacher_id", columnList = "teacher_id")})
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(
        name = "day_schedule_id",
        foreignKey = @ForeignKey(name = "fk_lessons_day_schedule"))
    private DaySchedule daySchedule;

    @NotNull
    @ManyToOne()
    @JoinColumn(
        name = "academic_hour_id",
        foreignKey = @ForeignKey(name = "fk_lessons_academic_hours"))
    private AcademicHour academicHour;

    @NotNull
    @ManyToOne()
    @JoinColumn(
        name = "classroom_id",
        foreignKey = @ForeignKey(name = "fk_lessons_classrooms"))
    private Classroom classroom;

    @NotNull
    @ManyToOne()
    @JoinColumn(
        name = "group_id",
        foreignKey = @ForeignKey(name = "fk_lessons_groups"))
    private Group group;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "course_id",
        foreignKey = @ForeignKey(name = "fk_lessons_courses"))
    private Course course;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "teacher_id",
        foreignKey = @ForeignKey(name = "fk_lessons_teachers"))
    private Teacher teacher;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @CreationTimestamp
    private LocalDateTime createAt;

    public Lesson(@NotNull DaySchedule daySchedule,
                  @NotNull AcademicHour academicHour,
                  @NotNull Classroom classroom,
                  @NotNull Group group,
                  @NotNull Course course,
                  @NotNull Teacher teacher) {
        this.daySchedule = daySchedule;
        this.academicHour = academicHour;
        this.classroom = classroom;
        this.group = group;
        this.course = course;
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lesson lesson = (Lesson) o;
        return getId() != null && Objects.equals(getId(), lesson.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
