package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.model.repository.CourseRepository;
import ua.englishschool.backend.model.service.CourseService;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course create(Course course) {
        if (course.getId() == 0) {
            return courseRepository.saveAndFlush(course);
        }
        return course;
    }

    @Override
    public boolean update(Course course) {
        if (isExists(course.getId())) {
            courseRepository.saveAndFlush(course);
            return true;
        }
        return false;
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getById(long id) {
        return courseRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return courseRepository.existsById(id);
    }
}
