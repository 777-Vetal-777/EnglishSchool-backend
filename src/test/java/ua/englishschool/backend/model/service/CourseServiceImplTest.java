package ua.englishschool.backend.model.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.model.repository.CourseRepository;
import ua.englishschool.backend.model.service.impl.CourseServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    private static final long COURSE_ID = 1;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(COURSE_ID);
        course.setName("SPRING");
    }

    @Test
    void whenCreate_thenReturnCourse() {
        course.setId(0);
        when(courseRepository.saveAndFlush(course)).thenReturn(course);

        courseService.create(course);

        verify(courseRepository).saveAndFlush(course);

    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(courseRepository.existsById(COURSE_ID)).thenReturn(true);

        boolean result = courseService.update(course);

        verify(courseRepository).saveAndFlush(course);
        verify(courseRepository).existsById(COURSE_ID);
        assertTrue(result);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(courseRepository.existsById(COURSE_ID)).thenReturn(false);

        boolean result = courseService.update(course);

        assertFalse(result);
        verify(courseRepository, never()).saveAndFlush(course);
        verify(courseRepository).existsById(COURSE_ID);

    }

    @Test
    void whenGetAll_thenReturnList() {
        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));

        List<Course> result = courseService.getAll();

        assertEquals(Collections.singletonList(course), result);
    }

    @Test
    void whenGetById_thenReturnCourse() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.ofNullable(course));

        Optional<Course> result = courseService.getById(COURSE_ID);

        assertEquals(course, result.get());
        verify(courseRepository).findById(COURSE_ID);
    }

    @Test
    void whenDelete_thenReturnTrue() {
        when(courseRepository.existsById(COURSE_ID)).thenReturn(true);

        boolean result = courseService.delete(COURSE_ID);

        assertTrue(result);
        verify(courseRepository).existsById(COURSE_ID);
        verify(courseRepository).deleteById(COURSE_ID);
    }

    @Test
    void whenDelete_thenReturnFalse() {
        when(courseRepository.existsById(COURSE_ID)).thenReturn(false);

        boolean result = courseService.delete(COURSE_ID);

        assertFalse(result);
        verify(courseRepository).existsById(COURSE_ID);
        verify(courseRepository, never()).deleteById(COURSE_ID);
    }

    @Test
    void whenExist_thenReturnTrue() {
        when(courseRepository.existsById(COURSE_ID)).thenReturn(true);

        courseService.isExists(COURSE_ID);

        verify(courseRepository).existsById(COURSE_ID);
    }
}
