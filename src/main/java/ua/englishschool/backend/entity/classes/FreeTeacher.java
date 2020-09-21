package ua.englishschool.backend.entity.classes;

import ua.englishschool.backend.entity.Teacher;

import java.util.Objects;

public class FreeTeacher {

    private Teacher teacher;

    private int amountCourses;

    public FreeTeacher() {
    }

    public FreeTeacher(Teacher teacher, int amountCourses) {
        this.teacher = teacher;
        this.amountCourses = amountCourses;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getAmountCourses() {
        return amountCourses;
    }

    public void setAmountCourses(int amountCourses) {
        this.amountCourses = amountCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeTeacher that = (FreeTeacher) o;
        return amountCourses == that.amountCourses &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacher, amountCourses);
    }

    @Override
    public String toString() {
        return "FreeTeacher{" +
                "teacher=" + teacher +
                ", amountCourses=" + amountCourses +
                '}';
    }
}
