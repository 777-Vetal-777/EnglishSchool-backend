package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.model.repository.StudentRepository;
import ua.englishschool.backend.model.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Student create(Student student) {
        if (student.getId() == 0) {
            return studentRepository.saveAndFlush(student);
        }
        return student;
    }

    @Override
    public boolean update(Student student) {
        if (isExists(student.getId())) {
            studentRepository.saveAndFlush(student);
            return true;
        }
        return false;
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getById(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return studentRepository.existsById(id);
    }
}