package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.PeriodTime;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.ContractDto;
import ua.englishschool.backend.entity.dto.CreateContractDto;
import ua.englishschool.backend.model.repository.ContractRepository;
import ua.englishschool.backend.model.service.impl.ContractServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentInvoiceService studentInvoiceService;

    private static final long CONTRACT_ID = 1;

    private static final long COURSE_ID = 2;

    private static final long STUDENT_ID = 3;

    private static final String PHONE = "12345";

    private Contract contract;

    private Course course;

    private Student student;

    private Teacher teacher;

    private CreateContractDto createContractDto;

    private PeriodDate periodDate;

    private PeriodTime periodTime;

    private ContractDto contractDto;

    @BeforeEach
    void setUp() {
        periodDate = new PeriodDate();
        periodDate.setStartDate(LocalDate.now());
        periodDate.setEndDate(LocalDate.now());

        periodTime = new PeriodTime();
        periodTime.setStartTime(LocalTime.now());
        periodTime.setEndTime(LocalTime.now());

        teacher = new Teacher();
        teacher.setLastName("LastName");
        teacher.setFirstName("FirstName");

        course = new Course();
        course.setId(COURSE_ID);
        course.setPeriodDate(periodDate);
        course.setPeriodTime(periodTime);
        course.setTeacher(teacher);

        student = new Student();
        student.setId(STUDENT_ID);
        student.setPhoneNumber(PHONE);

        createContractDto = new CreateContractDto();
        createContractDto.setStudentId(STUDENT_ID);
        createContractDto.setCourseId(COURSE_ID);

        contract = new Contract();
        contract.setId(CONTRACT_ID);
        contract.setCourse(course);
        contract.setStudent(student);
        contract.setContractStatusType(ContractStatusType.OPEN);

        contractDto = new ContractDto();
        contractDto.setCourseId(COURSE_ID);
        contractDto.setStartDate(course.getPeriodDate().getStartDate());
        contractDto.setEndDate(course.getPeriodDate().getEndDate());
        contractDto.setEndTime(course.getPeriodTime().getEndTime());
        contractDto.setStartTime(course.getPeriodTime().getStartTime());
        contractDto.setPhone(student.getPhoneNumber());
        contractDto.setTeacherName(course.getTeacher().getFirstName().concat(" " + course.getTeacher().getLastName()));

    }

    @Test
    void whenCreate_thenReturnContract() {
        contract.setId(0);
        when(contractRepository.saveAndFlush(contract)).thenReturn(contract);

        contractService.create(contract);

        verify(contractRepository).saveAndFlush(contract);

    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        boolean result = contractService.update(contract);

        verify(contractRepository).saveAndFlush(contract);
        verify(contractRepository).existsById(CONTRACT_ID);
        assertTrue(result);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(false);

        boolean result = contractService.update(contract);

        assertFalse(result);
        verify(contractRepository, never()).saveAndFlush(contract);
        verify(contractRepository).existsById(CONTRACT_ID);

    }

    @Test
    void whenGetAll_thenReturnList() {
        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        List<Contract> result = contractService.getAll();

        assertEquals(Collections.singletonList(contract), result);
    }

    @Test
    void whenGetById_thenReturnContract() {
        when(contractRepository.findById(CONTRACT_ID)).thenReturn(Optional.ofNullable(contract));

        Optional<Contract> result = contractService.getById(CONTRACT_ID);

        assertEquals(contract, result.get());
        verify(contractRepository).findById(CONTRACT_ID);
    }

    @Test
    void whenDelete_thenReturnTrue() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        boolean result = contractService.delete(CONTRACT_ID);

        assertTrue(result);
        verify(contractRepository).existsById(CONTRACT_ID);
        verify(contractRepository).deleteById(CONTRACT_ID);
    }

    @Test
    void whenDelete_thenReturnFalse() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(false);

        boolean result = contractService.delete(CONTRACT_ID);

        assertFalse(result);
        verify(contractRepository).existsById(CONTRACT_ID);
        verify(contractRepository, never()).deleteById(CONTRACT_ID);
    }

    @Test
    void whenExist_thenReturnTrue() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        contractService.isExists(CONTRACT_ID);

        verify(contractRepository).existsById(CONTRACT_ID);
    }

    @Test
    void whenGetAllByStatus_thenReturnList() {
        when(contractRepository.findAllByContractStatusType(ContractStatusType.OPEN)).thenReturn(Collections.singletonList(contract));

        List<Contract> result = contractService.getAllByStatus(ContractStatusType.OPEN);

        assertEquals(Collections.singletonList(contract), result);
        verify(contractRepository).findAllByContractStatusType(ContractStatusType.OPEN);
    }

    @Test
    void whenGetAllByStatusAndCourse_thenReturnList() {
        when(contractRepository.findAllByCourseAndContractStatusType(course, ContractStatusType.OPEN)).thenReturn(Collections.singletonList(contract));

        List<Contract> result = contractService.getAllByCourseAndStatusType(course, ContractStatusType.OPEN);

        assertEquals(Collections.singletonList(contract), result);
        verify(contractRepository).findAllByCourseAndContractStatusType(course, ContractStatusType.OPEN);
    }

    @Test
    void whenFindByStudentAndStatus_thenReturnContract() {
        when(contractRepository.findContractByStudentAndContractStatusType(student, ContractStatusType.OPEN)).thenReturn(Optional.ofNullable(contract));

        Optional<Contract> result = contractService.findContractByStudentAndContractStatusType(student, ContractStatusType.OPEN);

        assertEquals(contract, result.get());
    }

    @Test
    void whenFindCountByContractStatusOpenAndWaitAndCourse_thenReturnInt() {
        when(contractRepository.findCountByStatusOpenAndWaitAndCourse(course)).thenReturn(2);

        int result = contractService.countByContractStatusOpenAndWaitAndCourse(course);

        assertEquals(2, result);
    }


    @Test
    void createContract_thenReturnId() {
        contract.setId(0);
        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        when(courseService.getById(COURSE_ID)).thenReturn(Optional.ofNullable(course));
        when(studentService.getById(STUDENT_ID)).thenReturn(Optional.ofNullable(student));
        when(contractRepository.saveAndFlush(any(Contract.class))).thenReturn(contract);
        when(studentInvoiceService.generateStudentInvoices(any(Contract.class))).thenReturn(Collections.emptyList());
        long result = contractService.createContract(createContractDto);

        verify(contractRepository).saveAndFlush(captor.capture());
        assertEquals(contract, captor.getValue());
        assertEquals(0, result);

    }

    @Test
    void createContract_thenReturnException() {
        contract.setId(0);
        when(courseService.getById(COURSE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            contractService.createContract(createContractDto);
        });

    }

    @Test
    void findByPhone_thenReturnContractDto() {
        when(studentService.findStudentByPhone(PHONE)).thenReturn(Optional.ofNullable(student));
        when(contractRepository.findByStudentAndStatusTypeOpenOrWait(student)).thenReturn(Optional.ofNullable(contract));

        ContractDto result = contractService.findByPhone(PHONE);

        assertEquals(contractDto, result);
    }

    @Test
    void findByPhone_thenReturnException() {
        when(studentService.findStudentByPhone(PHONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            contractService.findByPhone(PHONE);
        });

    }

    @Test
    void whenFindByStudentAndStatusOpenOrWait_thenReturnContract() {
        when(contractRepository.findByStudentAndStatusTypeOpenOrWait(student)).thenReturn(Optional.ofNullable(contract));

        Optional<Contract> result = contractService.findByStudentAndStatusOpenOrWait(student);

        assertEquals(contract, result.get());
    }

    @Test
    void whenFindCountByCourseAndStatus_thenReturnContract() {
        when(contractRepository.findCountByCourseAndStatus(course, ContractStatusType.OPEN)).thenReturn(5);

        int result = contractService.countByCourseAndStatus(course, ContractStatusType.OPEN);

        assertEquals(5, result);
    }


}
