package ua.englishschool.backend.entity.dto;

import ua.englishschool.backend.entity.Course;

import java.util.Objects;

public class CourseDto {

    private Course course;

    private int availableStudents;

    public CourseDto(Course course, int availableStudents) {
        this.course = course;
        this.availableStudents = availableStudents;
    }

    public Course getCourse() {
        return course;
    }

    public CourseDto() {
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getAvailableStudents() {
        return availableStudents;
    }

    public void setAvailableStudents(int availableStudents) {
        this.availableStudents = availableStudents;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDto courseDto = (CourseDto) o;
        return availableStudents == courseDto.availableStudents &&
                Objects.equals(course, courseDto.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, availableStudents);
    }

    @Override
    public String toString() {
        return "CourseDto{" +
                "course=" + course +
                ", availableStudents=" + availableStudents +
                '}';
    }
}
