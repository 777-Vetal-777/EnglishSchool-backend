package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.model.repository.StudentRepository;
import ua.englishschool.backend.model.service.impl.StudentServiceImpl;

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
public class StudentServiceImplTest {

    private static final long STUDENT_ID = 1;

    private static final String PHONE = "777777777";

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName("Vitaliy");
        student.setPhoneNumber(PHONE);
    }

    @Test
    void whenCreate_thenReturnStudent() {
        student.setId(0);
        when(studentRepository.saveAndFlush(student)).thenReturn(student);

        studentService.create(student);

        verify(studentRepository).saveAndFlush(student);

    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(studentRepository.existsById(STUDENT_ID)).thenReturn(true);

        boolean result = studentService.update(student);

        verify(studentRepository).saveAndFlush(student);
        verify(studentRepository).existsById(STUDENT_ID);
        assertTrue(result);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(studentRepository.existsById(STUDENT_ID)).thenReturn(false);

        boolean result = studentService.update(student);

        assertFalse(result);
        verify(studentRepository, never()).saveAndFlush(student);
        verify(studentRepository).existsById(STUDENT_ID);

    }

    @Test
    void whenGetAll_thenReturnList() {
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(student));

        List<Student> result = studentService.getAll();

        assertEquals(Collections.singletonList(student), result);
    }

    @Test
    void whenGetById_thenReturnStudent() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.ofNullable(student));

        Optional<Student> result = studentService.getById(STUDENT_ID);

        assertEquals(student, result.get());
        verify(studentRepository).findById(STUDENT_ID);
    }

    @Test
    void whenDelete_thenReturnTrue() {
        when(studentRepository.existsById(STUDENT_ID)).thenReturn(true);

        boolean result = studentService.delete(STUDENT_ID);

        assertTrue(result);
        verify(studentRepository).existsById(STUDENT_ID);
        verify(studentRepository).deleteById(STUDENT_ID);
    }

    @Test
    void whenDelete_thenReturnFalse() {
        when(studentRepository.existsById(STUDENT_ID)).thenReturn(false);

        boolean result = studentService.delete(STUDENT_ID);

        assertFalse(result);
        verify(studentRepository).existsById(STUDENT_ID);
        verify(studentRepository, never()).deleteById(STUDENT_ID);
    }

    @Test
    void whenExist_thenReturnTrue() {
        when(studentRepository.existsById(STUDENT_ID)).thenReturn(true);

        studentService.isExists(STUDENT_ID);

        verify(studentRepository).existsById(STUDENT_ID);
    }

    @Test
    void whenFindStudentByPhone_thenReturnStudent() {
        when(studentRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.ofNullable(student));

        Optional<Student> result = studentService.findStudentByPhone(PHONE);

        assertEquals(student, result.get());
        verify(studentRepository).findByPhoneNumber(PHONE);
    }

}
