package ua.englishschool.backend.model.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.CourseDto;
import ua.englishschool.backend.model.repository.CourseRepository;
import ua.englishschool.backend.model.service.impl.CourseServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Mock
    private ContractService contractService;

    private static final long COURSE_ID = 1;

    private static final long CONTRACT_ID = 2;

    private Course course;

    private Course course2;

    private CourseDto courseDto;

    private CourseDto courseDto2;

    private Contract contract;

    private Contract contract2;

    private Set<CourseDto> courseDtoSet;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(COURSE_ID);
        course.setName("ELEMENTARY");
        course.setMaxCapacity(20);

        course2 = new Course();
        course2.setId(2);
        course2.setName("INTERMEDIATE");
        course2.setMaxCapacity(17);

        courseDto = new CourseDto();
        courseDto.setCourse(course);
        courseDto.setFreeVacancies(17);

        courseDto2 = new CourseDto();
        courseDto2.setCourse(course2);
        courseDto2.setFreeVacancies(14);

        contract = new Contract();
        contract.setId(CONTRACT_ID);
        contract.setCourse(course);

        contract2 = new Contract();
        contract2.setId(10);
        contract2.setCourse(course2);

        courseDtoSet = new HashSet<>();
        courseDtoSet.add(courseDto);
        courseDtoSet.add(courseDto2);

        teacher = new Teacher();
        teacher.setId(20);
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

    @Test
    void whenGetAllDtoOpenOrWaitAndCourse_thenReturnListCoursesDto() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));
        when(contractService.countByContractStatusOpenAndWaitAndCourse(course)).thenReturn(3);
        when(contractService.countByContractStatusOpenAndWaitAndCourse(course2)).thenReturn(3);

        List<CourseDto> result = courseService.getAllCoursesDtoOpenOrWait();

        assertEquals(Arrays.asList(courseDto, courseDto2), result);
    }

    @Test
    void whenGetAllDtoOpenOrWaitAndCourse_thenReturnEmptyList() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<CourseDto> result = courseService.getAllCoursesDtoOpenOrWait();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void whenGetAllActiveCourses_thenReturnListCoursesDto() {
        when(courseRepository.findAllByEndDateAfter(LocalDate.now())).thenReturn(Arrays.asList(course, course2));
        when(contractService.countByStatusOpenAndCourse(course)).thenReturn(3);
        when(contractService.countByStatusOpenAndCourse(course2)).thenReturn(3);

        Set<CourseDto> result = courseService.getAllActiveCourses();

        assertEquals(courseDtoSet, result);

    }

    @Test
    void whenGetAllWaitCourses_thenReturnListCoursesDto() {
        when(courseRepository.findAllByStartDateAfter(LocalDate.now())).thenReturn(Arrays.asList(course, course2));
        when(contractService.countByCourseAndStatus(course, ContractStatusType.WAIT)).thenReturn(3);
        when(contractService.countByCourseAndStatus(course2, ContractStatusType.WAIT)).thenReturn(3);

        List<CourseDto> courseDtoList = courseService.getAllWaitCourses();

        assertEquals(Arrays.asList(courseDto, courseDto2), courseDtoList);
    }

    @Test
    void whenCountCoursesByTeacher_thenReturnCount() {
        when(courseRepository.countByTeacherAndEndDateAfter(teacher, LocalDate.now())).thenReturn(5);

        int result = courseService.countCoursesByTeacher(teacher);

        assertEquals(5, result);
    }
}
