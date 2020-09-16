package ua.englishschool.backend.model.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.model.repository.TeacherRepository;
import ua.englishschool.backend.model.service.impl.TeacherServiceImpl;

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
public class TeacherServiceImplTest {

    private static final long TEACHER_ID = 1;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setFirstName("Teacher");
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

}
