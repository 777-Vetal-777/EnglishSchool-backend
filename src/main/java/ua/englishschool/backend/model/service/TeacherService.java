package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.dto.TeacherDto;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends GenericService<Teacher> {

    List<TeacherDto> getAllTeachersDto();

    TeacherDto findByPhoneDto(String phone);
}
