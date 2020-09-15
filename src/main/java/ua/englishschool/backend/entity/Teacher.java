package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn(name = "person_id")
public class Teacher extends User {

    @Column
    private long maxStudents;

    public long getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(long maxStudents) {
        this.maxStudents = maxStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return maxStudents == teacher.maxStudents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxStudents);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "maxStudents=" + maxStudents +
                "} " + super.toString();
    }
}
