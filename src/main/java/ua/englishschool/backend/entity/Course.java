package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private PeriodDate periodDate;

    @Column
    private PeriodTime periodTime;

    @ManyToOne
    @JoinColumn(name = "teacher_id", foreignKey = @ForeignKey(name = "FK_teachers_courses"))
    private Teacher teacher;

    @Column
    private int price;

    @Column
    private int maxCapacity;


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

    public PeriodDate getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(PeriodDate periodDate) {
        this.periodDate = periodDate;
    }

    public PeriodTime getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(PeriodTime periodTime) {
        this.periodTime = periodTime;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                price == course.price &&
                maxCapacity == course.maxCapacity &&
                Objects.equals(name, course.name) &&
                Objects.equals(periodDate, course.periodDate) &&
                Objects.equals(periodTime, course.periodTime) &&
                Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, periodDate, periodTime, teacher, price, maxCapacity);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", periodDate=" + periodDate +
                ", periodTime=" + periodTime +
                ", teacher=" + teacher +
                ", price=" + price +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
