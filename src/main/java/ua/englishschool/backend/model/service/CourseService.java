package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.dto.CourseDto;

import java.util.List;
import java.util.Set;

public interface CourseService extends GenericService<Course> {

    List<CourseDto> getAllCoursesDtoOpenOrWait();

    Set<CourseDto> getAllActiveCourses();
}
