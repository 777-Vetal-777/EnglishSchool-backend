package ua.englishschool.backend.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.dto.CourseDto;
import ua.englishschool.backend.model.service.CourseService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    private static final long COURSE_ID = 1;


    private static final String URL = "/courses";

    private static final String URL_GET_ALL_DTO = URL + "/get-all/dto";

    private static final String URL_GET_ALL_ACTIVE_DTO = URL + "/get-all/active/dto";

    private static final String URL_GET_ALL_WAIT_DTO = URL + "/get-all/wait/dto";


    @Autowired
    private MockMvc server;

    @MockBean
    private CourseService courseService;

    private Course course;

    private CourseDto courseDto;

    private Set<CourseDto> courseDtoSet;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(COURSE_ID);

        courseDto = new CourseDto();
        courseDto.setCourse(course);
        courseDto.setAvailableStudents(15);

        courseDtoSet = new HashSet<>();
        courseDtoSet.add(courseDto);
    }

    @Test
    void createCourse_ReturnId() throws Exception {
        when(courseService.create(course)).thenReturn(course);

        server.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(course)))
                .andDo(print())
                .andExpect(content().json(String.valueOf(COURSE_ID)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenGetAllDto_ReturnListCoursesDto() throws Exception {
        when(courseService.getAllCoursesDtoOpenOrWait()).thenReturn(Collections.singletonList(courseDto));

        server.perform(get(URL_GET_ALL_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(courseDto))))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllActiveDto_ReturnListCoursesDto() throws Exception {
        when(courseService.getAllActiveCourses()).thenReturn(courseDtoSet);

        server.perform(get(URL_GET_ALL_ACTIVE_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(courseDtoSet)))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllWaitDto_ReturnListCoursesDto() throws Exception {
        when(courseService.getAllWaitCourses()).thenReturn(Collections.singletonList(courseDto));

        server.perform(get(URL_GET_ALL_WAIT_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(courseDto))))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
