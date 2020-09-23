package ua.englishschool.backend.entity.dto;

import java.util.Objects;

public class CreateContractDto {

    private long studentId;

    private long courseId;

    public CreateContractDto(long studentId, long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public CreateContractDto() {
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateContractDto that = (CreateContractDto) o;
        return studentId == that.studentId &&
                courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }

    @Override
    public String toString() {
        return "CreateContractDto{" +
                "studentId=" + studentId +
                ", courseId=" + courseId +
                '}';
    }
}
