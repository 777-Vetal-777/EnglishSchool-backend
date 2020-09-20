package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.CourseDto;
import ua.englishschool.backend.model.repository.CourseRepository;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.CourseService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    private ContractService contractService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, ContractService contractService) {
        this.courseRepository = courseRepository;
        this.contractService = contractService;
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

    @Override
    public List<CourseDto> getAllCoursesDtoOpenOrWait() {
        List<Course> listCourses = getAll();
        List<CourseDto> listCoursesDto = new ArrayList<>();
        for (Course course : listCourses) {
            int countStudents = contractService.findCountByContractStatusOpenAndWaitAndCourse(course);
            listCoursesDto.add(createCourseDto(course, countStudents));
        }
        return listCoursesDto;
    }

    @Override
    public Set<CourseDto> getAllActiveCourses() {
        List<Contract> contractList = contractService.getAllByStatus(ContractStatusType.OPEN);
        Set<CourseDto> courseDtoList = new HashSet<>();
        Set<Course> courses = new HashSet<>();
        for (Contract contract : contractList) {
            courses.add(contract.getCourse());
        }
        for (Course course : courses) {
            int countStudents = contractService.findCountByStatusOpenAndCourse(course);
            courseDtoList.add(createCourseDto(course, countStudents));
        }
        return courseDtoList;
    }

    private CourseDto createCourseDto(Course course, int countStudents) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourse(course);
        courseDto.setAvailableStudents(course.getMaxCapacity() - countStudents);

        return courseDto;
    }
}
