package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.StudentDto;
import ua.englishschool.backend.model.repository.StudentRepository;
import ua.englishschool.backend.model.service.impl.StudentServiceImpl;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    private static final long STUDENT_ID = 1;

    private static final long CONTRACT_ID = 2;

    private static final long COURSE_ID = 3;

    private static final String PHONE = "777777777";

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ContractService contractService;

    private Student student;

    private Student student2;

    private Contract contract;

    private Contract contract2;

    private Course course;

    private StudentDto studentDto;


    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName("firstName");
        student.setLastName("lastName");
        student.setPhoneNumber(PHONE);
        student.setActive(true);

        student2 = new Student();
        student2.setId(5);
        student2.setFirstName("firstName2");

        course = new Course();
        course.setId(COURSE_ID);
        course.setName("Course");

        contract = new Contract();
        contract.setId(CONTRACT_ID);
        contract.setStudent(student);
        contract.setCourse(course);

        contract2 = new Contract();
        contract2.setStudent(student2);

        studentDto = new StudentDto();
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setPhoneNumber(student.getPhoneNumber());
        studentDto.setCourseName(course.getName());


    }

    @Test
    void whenCreate_thenReturnStudent() {
        student.setId(0);
        when(studentRepository.saveAndFlush(student)).thenReturn(student);
        when(studentRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.empty());

        studentService.create(student);

        verify(studentRepository).saveAndFlush(student);

    }

    @Test
    void whenCreate_thenThrowException() {

        when(studentRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.ofNullable(student));

        assertThrows(EntityExistsException.class,
                () -> {
                    studentService.create(student);
                });

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

    @Test
    void whenFindStudentByPhoneDto_thenReturnStudentDto() {
        when(studentRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.ofNullable(student));
        when(contractService.findContractByStudentAndContractStatusType(student, ContractStatusType.OPEN)).thenReturn(Optional.ofNullable(contract));

        Optional<StudentDto> result = studentService.findStudentDtoByPhone(PHONE);

        assertEquals(studentDto, result.get());
        verify(studentRepository).findByPhoneNumber(PHONE);
        verify(contractService).findContractByStudentAndContractStatusType(student, ContractStatusType.OPEN);
    }

    @Test
    void whenFindStudentByPhoneDto_thenTrowException() {
        when(studentRepository.findByPhoneNumber(PHONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> {
                    studentService.findStudentDtoByPhone(PHONE);
                });

    }

    @Test
    void whenFindActiveStudents_thenReturnListStudentsDto() {
        when(contractService.getAllByStatus(ContractStatusType.OPEN)).thenReturn(Collections.singletonList(contract));

        List<StudentDto> result = studentService.findActiveStudentsDto();

        assertEquals(Collections.singletonList(studentDto), result);
    }

    @Test
    void whenFindActiveStudents_thenReturnListEmpty() {
        when(contractService.getAllByStatus(ContractStatusType.OPEN)).thenReturn(Collections.emptyList());

        List<StudentDto> result = studentService.findActiveStudentsDto();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void whenFindAllByActiveFalseDto_thenReturnListStudents() {
        studentDto.setCourseName(null);
        when(studentRepository.findAllByActive(false)).thenReturn(Collections.singletonList(student));

        List<StudentDto> result = studentService.findAllByActiveFalseDto();

        assertEquals(Collections.singletonList(studentDto), result);
    }

    @Test
    void whenFindAllStudentsDto_thenReturnListStudents() {
        StudentDto studentDto2 = new StudentDto();
        studentDto2.setFirstName("firstName2");
        when(studentRepository.findAllByActive(false)).thenReturn(Collections.singletonList(student2));
        when(studentRepository.findAllByActive(true)).thenReturn(Collections.singletonList(student));
        when(contractService.findByStudentAndStatusOpenOrWait(student)).thenReturn(Optional.ofNullable(contract));

        List<StudentDto> result = studentService.findAllStudentsDto();

        assertEquals(Arrays.asList(studentDto, studentDto2), result);
    }

}
