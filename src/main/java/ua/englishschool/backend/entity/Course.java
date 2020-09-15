package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private Period period;

    @OneToOne
    @JoinColumn(name = "teacher_id", foreignKey = @ForeignKey(name = "FK_teacher_course"))
    private Teacher teacher;

    @Column
    private Long amount;

    @Column(name = "teacher_share")
    private Long teacherShare;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getTeacherShare() {
        return teacherShare;
    }

    public void setTeacherShare(Long teacherShare) {
        this.teacherShare = teacherShare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                Objects.equals(name, course.name) &&
                Objects.equals(period, course.period) &&
                Objects.equals(teacher, course.teacher) &&
                Objects.equals(amount, course.amount) &&
                Objects.equals(teacherShare, course.teacherShare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, period, teacher, amount, teacherShare);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", period=" + period +
                ", teacher=" + teacher +
                ", amount=" + amount +
                ", teacherShare=" + teacherShare +
                '}';
    }
}
