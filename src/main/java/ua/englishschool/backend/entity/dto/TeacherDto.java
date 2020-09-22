package ua.englishschool.backend.entity.dto;

import java.util.Objects;

public class TeacherDto {

    private long teacherId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private int maxCourses;

    private int countCourses;

    private boolean active;


    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getMaxCourses() {
        return maxCourses;
    }

    public void setMaxCourses(int maxCourses) {
        this.maxCourses = maxCourses;
    }

    public int getCountCourses() {
        return countCourses;
    }

    public void setCountCourses(int countCourses) {
        this.countCourses = countCourses;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherDto that = (TeacherDto) o;
        return teacherId == that.teacherId &&
                maxCourses == that.maxCourses &&
                countCourses == that.countCourses &&
                active == that.active &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, firstName, lastName, phoneNumber, maxCourses, countCourses, active);
    }

    @Override
    public String toString() {
        return "TeacherDto{" +
                "teacherId=" + teacherId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", maxCourses=" + maxCourses +
                ", countCourses=" + countCourses +
                ", active=" + active +
                '}';
    }
}
