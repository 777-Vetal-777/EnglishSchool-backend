package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.classes.FreeTeacher;
import ua.englishschool.backend.entity.dto.TeacherDto;
import ua.englishschool.backend.model.repository.TeacherRepository;
import ua.englishschool.backend.model.service.CourseService;
import ua.englishschool.backend.model.service.TeacherService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseService courseService;

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
        return teacherRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return teacherRepository.existsById(id);
    }

    @Override
    public List<TeacherDto> getAllTeachersDto() {
        return getAll().stream()
                .map(teacher -> new TeacherDto(teacher.getFirstName(), teacher.getLastName(), teacher.getPhoneNumber(),
                        teacher.getMaxCourses(), courseService.countCoursesByTeacher(teacher)))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDto findByPhoneDto(String phone) {
        Optional<Teacher> teacher = teacherRepository.findByPhoneNumber(phone);
        teacher.orElseThrow(() -> new EntityNotFoundException("Teacher was not found with phone: " + phone));

        return new TeacherDto(teacher.get().getFirstName(), teacher.get().getLastName(), teacher.get().getPhoneNumber(),
                teacher.get().getMaxCourses(), courseService.countCoursesByTeacher(teacher.get()));
    }

    @Override
    public Optional<Teacher> findRandomFreeTeacher() {
        List<Teacher> activeTeacher = findByActive(true);
        List<FreeTeacher> freeTeachers = new ArrayList<>();
        int countCourses = 0;
        for (Teacher teacher : activeTeacher) {
            countCourses = courseService.countCoursesByTeacher(teacher);
            if (teacher.getMaxCourses() > countCourses) {
                freeTeachers.add(new FreeTeacher(teacher, countCourses));
            }
        }

        return Optional.ofNullable(getRandomTeacher(freeTeachers));
    }

    private Teacher getRandomTeacher(List<FreeTeacher> freeTeachers) {
        List<FreeTeacher> freeTeachersSame = new ArrayList<>();
        if (freeTeachers.isEmpty()) {
            throw new EntityNotFoundException("Teacher was not found");
        }
        FreeTeacher freeTeacher = freeTeachers.get(0);
        for (int i = 0; i < freeTeachers.size(); i++) {
            if (freeTeachers.get(i).getAmountCourses() == freeTeacher.getAmountCourses()) {
                freeTeachersSame.add(freeTeachers.get(i));
            }
            if (freeTeachers.get(i).getAmountCourses() < freeTeacher.getAmountCourses()) {
                freeTeacher = freeTeachers.get(i);
                freeTeachersSame.clear();
                freeTeachersSame.add(freeTeachers.get(i));
            }
        }
        int teacherIdInList = (int) (Math.random() * freeTeachersSame.size());
        Teacher teacher = freeTeachersSame.get(teacherIdInList).getTeacher();

        return teacher;
    }


    @Override
    public List<Teacher> findByActive(boolean active) {
        return teacherRepository.findAllByActive(true);
    }
}