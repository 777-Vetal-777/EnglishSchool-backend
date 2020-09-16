package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Student;

import java.util.Optional;

public interface StudentService extends GenericService<Student> {

    Optional<Student> findStudentByPhone(String phone);
}
