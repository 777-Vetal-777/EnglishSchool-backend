package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.model.repository.TeacherRepository;
import ua.englishschool.backend.model.service.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public Teacher create(Teacher teacher) {
        if (teacher.getId() == 0) {
            teacherRepository.saveAndFlush(teacher);
        }
        return teacher;
    }

    @Override
    public boolean update(Teacher teacher) {
        if (isExists(teacher.getId())) {
            teacherRepository.saveAndFlush(teacher);
            return true;
        }
        return false;
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return false;
    }
}
