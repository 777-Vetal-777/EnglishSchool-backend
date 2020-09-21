package ua.englishschool.backend.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.dto.CourseDto;
import ua.englishschool.backend.model.service.CourseService;
import ua.englishschool.backend.model.service.TeacherService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class CourseController {

    private static final String URL = "/courses";

    private static final String URL_GET_ALL_DTO = URL + "/get-all/dto";

    private static final String URL_GET_ALL_ACTIVE_DTO = URL + "/get-all/active/dto";

    private static final String URL_GET_ALL_WAIT_DTO = URL + "/get-all/wait/dto";

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long createCourse(@RequestBody Course course) {
        Optional<Teacher> teacher = teacherService.findRandomFreeTeacher();
        teacher.orElseThrow(() -> new EntityNotFoundException("Free Teacher was not found"));
        course.setTeacher(teacher.get());
        return courseService.create(course).getId();
    }

    @GetMapping(URL_GET_ALL_DTO)
    public List<CourseDto> getAllDto() {
        return courseService.getAllCoursesDtoOpenOrWait();
    }

    @GetMapping(URL_GET_ALL_ACTIVE_DTO)
    public Set<CourseDto> getAllActiveDto() {
        return courseService.getAllActiveCourses();
    }

    @GetMapping(URL_GET_ALL_WAIT_DTO)
    public List<CourseDto> getAllWaitDto() {
        return courseService.getAllWaitCourses();
    }
}
