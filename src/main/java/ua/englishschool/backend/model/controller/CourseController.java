package ua.englishschool.backend.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.dto.CourseDto;
import ua.englishschool.backend.model.service.CourseService;

import java.util.List;
import java.util.Set;

@RestController
public class CourseController {

    private static final String URL = "/courses";

    private static final String URL_GET_ALL_DTO = URL + "/get-all/dto";

    private static final String URL_GET_ALL_ACTIVE_DTO = URL + "/get-all/active/dto";

    private static final String URL_GET_ALL_WAIT_DTO = URL + "/get-all/wait/dto";

    @Autowired
    private CourseService courseService;

    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long createCourse(@RequestBody Course course) {
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
