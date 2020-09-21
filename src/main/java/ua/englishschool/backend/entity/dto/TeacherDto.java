package ua.englishschool.backend.entity.dto;

import java.util.Objects;

public class TeacherDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private int maxCourses;

    private int countCourses;

    public TeacherDto(String firstName, String lastName, String phoneNumber, int maxCourses, int countCourses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.maxCourses = maxCourses;
        this.countCourses = countCourses;
    }

    public TeacherDto() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherDto that = (TeacherDto) o;
        return maxCourses == that.maxCourses &&
                countCourses == that.countCourses &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber, maxCourses, countCourses);
    }

    @Override
    public String toString() {
        return "TeacherDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", maxCourses=" + maxCourses +
                ", countCourses=" + countCourses +
                '}';
    }
}
