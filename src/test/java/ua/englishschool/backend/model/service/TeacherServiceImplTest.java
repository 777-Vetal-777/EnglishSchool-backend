package ua.englishschool.backend.model.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.dto.TeacherDto;
import ua.englishschool.backend.model.repository.TeacherRepository;
import ua.englishschool.backend.model.service.impl.TeacherServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    private static final long TEACHER_ID = 1;

    private static final String PHONE = "12345";

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseService courseService;

    private Teacher teacher;

    private Teacher teacher2;

    private TeacherDto teacherDto;

    private TeacherDto teacherDto2;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setFirstName("Teacher");
        teacher.setLastName("LastName");
        teacher.setPhoneNumber(PHONE);
        teacher.setMaxCourses(5);

        teacher2 = new Teacher();
        teacher2.setId(10);
        teacher2.setFirstName("Teacher2");
        teacher2.setLastName("LastName2");
        teacher2.setPhoneNumber("54321");
        teacher2.setMaxCourses(5);

        teacherDto = new TeacherDto();
        teacherDto.setTeacherId(TEACHER_ID);
        teacherDto.setFirstName("Teacher");
        teacherDto.setLastName("LastName");
        teacherDto.setPhoneNumber("12345");
        teacherDto.setCountCourses(3);
        teacherDto.setMaxCourses(5);

        teacherDto2 = new TeacherDto();
        teacherDto2.setTeacherId(10);
        teacherDto2.setFirstName("Teacher2");
        teacherDto2.setLastName("LastName2");
        teacherDto2.setPhoneNumber("54321");
        teacherDto2.setCountCourses(5);
        teacherDto2.setMaxCourses(5);
    }

    @Test
    void whenCreate_thenReturnTeacher() {
        teacher.setId(0);
        when(teacherRepository.saveAndFlush(teacher)).thenReturn(teacher);

        teacherService.create(teacher);

        verify(teacherRepository).saveAndFlush(teacher);
    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(true);

        boolean result = teacherService.update(teacher);

        assertTrue(result);
        verify(teacherRepository).saveAndFlush(teacher);
        verify(teacherRepository).existsById(TEACHER_ID);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(false);

        boolean result = teacherService.update(teacher);

        assertFalse(result);
        verify(teacherRepository).existsById(TEACHER_ID);
        verify(teacherRepository, never()).saveAndFlush(teacher);
    }

    @Test
    void whenGetAll_thenReturnList() {
        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacher));

        List<Teacher> result = teacherService.getAll();

        assertEquals(Collections.singletonList(teacher), result);
    }

    @Test
    void whenGetById_thenReturnTeacher() {
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.ofNullable(teacher));

        Optional<Teacher> result = teacherService.getById(TEACHER_ID);

        assertEquals(teacher, result.get());
        verify(teacherRepository).findById(TEACHER_ID);
    }

    @Test
    void whenDelete_thenReturnTrue() {
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(true);

        boolean result = teacherService.delete(TEACHER_ID);

        assertTrue(result);
        verify(teacherRepository).existsById(TEACHER_ID);
        verify(teacherRepository).deleteById(TEACHER_ID);
    }

    @Test
    void whenDelete_thenReturnFalse() {
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(false);

        boolean result = teacherService.delete(TEACHER_ID);

        assertFalse(result);
        verify(teacherRepository).existsById(TEACHER_ID);
        verify(teacherRepository, never()).deleteById(TEACHER_ID);
    }

    @Test
    void whenExist_thenReturnTrue() {
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(true);

        boolean result = teacherService.isExists(TEACHER_ID);

        assertTrue(result);
        verify(teacherRepository).existsById(TEACHER_ID);
    }

    @Test
    void whenGetAllTeachersDto_thenReturnList() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher, teacher2));
        when(courseService.countCoursesByTeacher(teacher)).thenReturn(3);
        when(courseService.countCoursesByTeacher(teacher2)).thenReturn(5);

        List<TeacherDto> result = teacherService.getAllTeachersDto();

        assertEquals(Arrays.asList(teacherDto, teacherDto2), result);
        verify(teacherRepository).findAll();
        verify(courseService, times(2)).countCoursesByTeacher(any(Teacher.class));
    }

    @Test
    void whenFindByPhoneDto_thenReturnTeacherDto() {
        when(teacherRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.ofNullable(teacher));
        when(courseService.countCoursesByTeacher(teacher)).thenReturn(3);

        TeacherDto result = teacherService.findByPhoneDto(PHONE);

        assertEquals(teacherDto, result);
    }

    @Test
    void whenFindByActive_thenReturnList() {
        when(teacherRepository.findAllByActive(true)).thenReturn(Arrays.asList(teacher, teacher2));

        List<Teacher> result = teacherService.findByActive(true);

        assertEquals(Arrays.asList(teacher, teacher2), result);
    }

    @Test
    void whenFindRandomFreeTeacher_thenReturnTeacherDifferentCount() {
        when(teacherRepository.findAllByActive(true)).thenReturn(Arrays.asList(teacher, teacher2));
        when(courseService.countCoursesByTeacher(teacher)).thenReturn(3);
        when(courseService.countCoursesByTeacher(teacher2)).thenReturn(2);

        Optional<Teacher> result = teacherService.findRandomFreeTeacher();

        assertEquals(teacher2, result.get());
    }

    @Test
    void whenFindRandomFreeTeacher_thenReturnTeacherSameCount() {
        when(teacherRepository.findAllByActive(true)).thenReturn(Arrays.asList(teacher, teacher2));
        when(courseService.countCoursesByTeacher(teacher)).thenReturn(3);
        when(courseService.countCoursesByTeacher(teacher2)).thenReturn(3);

        boolean check1 = false;
        boolean check2 = false;
        Optional<Teacher> result;
        for (int i = 0; i < 20; i++) {
            result = teacherService.findRandomFreeTeacher();
            if (result.get().equals(teacher)) {
                check1 = true;
            }
            if (result.get().equals(teacher2)) {
                check2 = true;
            }
        }
        assertEquals(true, check1);
        assertEquals(true, check2);

    }

    @Test
    void whenChangeStatusActive_thenReturnTrue() {
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.ofNullable(teacher));
        when(teacherRepository.existsById(TEACHER_ID)).thenReturn(true);

        boolean active = teacherService.changeStatusActive(TEACHER_ID);

        verify(teacherRepository).saveAndFlush(captor.capture());
        assertTrue(captor.getValue().isActive());
        assertTrue(active);
    }


    @Test
    void whenFindRandomFreeTeacher_thenReturnException() {
        when(teacherRepository.findAllByActive(true)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () ->
        {
            teacherService.findRandomFreeTeacher();
        });

    }

    @Test
    void whenFindByPhone_thenReturnTeacher() {
        when(teacherRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.ofNullable(teacher));

        Optional<Teacher> result = teacherService.findByPhone(PHONE);

        assertEquals(teacher, result.get());
    }

    @Test
    void whenFindByActiveDto_thenReturnListDto() {
        when(teacherRepository.findAllByActive(true)).thenReturn(Arrays.asList(teacher, teacher2));
        when(courseService.countCoursesByTeacher(teacher)).thenReturn(3);
        when(courseService.countCoursesByTeacher(teacher2)).thenReturn(5);
        List<TeacherDto> teacherDtos = teacherService.findByActiveDto(true);

        assertEquals(Arrays.asList(teacherDto, teacherDto2), teacherDtos);
    }

}
