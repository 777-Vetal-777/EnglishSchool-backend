package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "teachers")
public class Teacher extends User {

    @Column
    private int maxCourses;

    public int getMaxCourses() {
        return maxCourses;
    }

    public void setMaxCourses(int maxCourses) {
        this.maxCourses = maxCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return maxCourses == teacher.maxCourses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxCourses);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "maxCourses=" + maxCourses +
                '}';
    }
}
