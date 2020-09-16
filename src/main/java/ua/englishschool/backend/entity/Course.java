package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
                Objects.equals(period, course.period) &&
                Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, period, teacher, price, maxCapacity);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", period=" + period +
                ", teacher=" + teacher +
                ", price=" + price +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
