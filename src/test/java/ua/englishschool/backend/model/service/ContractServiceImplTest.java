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
import ua.englishschool.backend.model.repository.ContractRepository;
import ua.englishschool.backend.model.service.impl.ContractServiceImpl;

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
public class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    private static final long CONTRACT_ID = 1;

    private static final long COURSE_ID = 2;

    private static final long STUDENT_ID = 3;

    private Contract contract;

    private Course course;

    private Student student;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(CONTRACT_ID);

        course = new Course();
        course.setId(COURSE_ID);

        student = new Student();
        student.setId(STUDENT_ID);
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
