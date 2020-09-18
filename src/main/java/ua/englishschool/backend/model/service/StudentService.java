package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.dto.StudentDto;

import java.util.List;
import java.util.Optional;

public interface StudentService extends GenericService<Student> {

    Optional<Student> findStudentByPhone(String phone);

    Optional<StudentDto> findStudentDtoByPhone(String phone);

    List<StudentDto> findActiveStudentsDto();

}
