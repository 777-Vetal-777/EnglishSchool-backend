package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.core.StudentInvoiceType;
import ua.englishschool.backend.entity.dto.StudentInvoiceDto;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.repository.StudentInvoiceRepository;
import ua.englishschool.backend.model.service.impl.StudentInvoiceServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentInvoiceServiceImplTest {

    @InjectMocks
    private StudentInvoiceServiceImpl studentInvoiceService;

    @Mock
    private StudentInvoiceRepository studentInvoiceRepository;

    @Mock
    private ContractService contractService;

    @Mock
    private StudentService studentService;

    private StudentInvoice studentInvoice;

    private StudentInvoice studentInvoice2;

    private Contract contract;

    private StudentInvoiceDto studentInvoiceDto;

    private PeriodDate periodDate;

    private Student student;

    private Course course;

    private static final long STUDENT_INVOICE_ID = 1;
    private static final String PHONE = "12345";

    @BeforeEach
    void setUp() {

        student = new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setPhoneNumber(PHONE);
        periodDate = new PeriodDate();
        periodDate.setStartDate(LocalDate.parse("2020-09-10"));
        periodDate.setEndDate(LocalDate.parse("2020-09-25"));

        course = new Course();
        course.setName("Course1");
        course.setPrice(1000);
        course.setPeriodDate(periodDate);

        studentInvoice = new StudentInvoice();
        studentInvoice.setId(STUDENT_INVOICE_ID);
        studentInvoice.setMoney(10000);
        studentInvoice.setPeriod(periodDate);
        studentInvoice.setType(StudentInvoiceType.WAIT);

        contract = new Contract();
        contract.setId(1);
        contract.setStudentInvoices(Arrays.asList(studentInvoice));
        contract.setStudent(student);
        contract.setCourse(course);
        contract.setContractStatusType(ContractStatusType.WAIT);

        studentInvoiceDto = new StudentInvoiceDto();
        studentInvoiceDto.setInvoiceId(1);
        studentInvoiceDto.setMoney(10000);
        studentInvoiceDto.setStartDate(periodDate.getStartDate());
        studentInvoiceDto.setEndDate(periodDate.getEndDate());
        studentInvoiceDto.setFirstName(student.getFirstName());
        studentInvoiceDto.setLastName(student.getLastName());
        studentInvoiceDto.setPhone(student.getPhoneNumber());
        studentInvoiceDto.setCourseName(course.getName());

        studentInvoice2 = new StudentInvoice();
        studentInvoice2.setType(StudentInvoiceType.WAIT);
        studentInvoice2.setPeriod(periodDate);
        studentInvoice2.setMoney(858);


    }

    @Test
    void whenCreate_thenReturnStudentInvoice() {
        studentInvoice.setId(0);
        when(studentInvoiceRepository.saveAndFlush(studentInvoice)).thenReturn(studentInvoice);

        studentInvoiceService.create(studentInvoice);

        verify(studentInvoiceRepository).saveAndFlush(studentInvoice);

    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(true);

        boolean result = studentInvoiceService.update(studentInvoice);

        verify(studentInvoiceRepository).saveAndFlush(studentInvoice);
        verify(studentInvoiceRepository).existsById(STUDENT_INVOICE_ID);
        assertTrue(result);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(false);

        boolean result = studentInvoiceService.update(studentInvoice);

        assertFalse(result);
        verify(studentInvoiceRepository, never()).saveAndFlush(studentInvoice);
        verify(studentInvoiceRepository).existsById(STUDENT_INVOICE_ID);

    }

    @Test
    void whenGetAll_thenReturnList() {
        when(studentInvoiceRepository.findAll()).thenReturn(Collections.singletonList(studentInvoice));

        List<StudentInvoice> result = studentInvoiceService.getAll();

        assertEquals(Collections.singletonList(studentInvoice), result);
    }

    @Test
    void whenGetById_thenReturnStudentInvoice() {
        when(studentInvoiceRepository.findById(STUDENT_INVOICE_ID)).thenReturn(Optional.ofNullable(studentInvoice));

        Optional<StudentInvoice> result = studentInvoiceService.getById(STUDENT_INVOICE_ID);

        assertEquals(studentInvoice, result.get());
        verify(studentInvoiceRepository).findById(STUDENT_INVOICE_ID);
    }

    @Test
    void whenDelete_thenReturnTrue() {
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(true);

        boolean result = studentInvoiceService.delete(STUDENT_INVOICE_ID);

        assertTrue(result);
        verify(studentInvoiceRepository).existsById(STUDENT_INVOICE_ID);
        verify(studentInvoiceRepository).deleteById(STUDENT_INVOICE_ID);
    }

    @Test
    void whenDelete_thenReturnFalse() {
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(false);

        boolean result = studentInvoiceService.delete(STUDENT_INVOICE_ID);

        assertFalse(result);
        verify(studentInvoiceRepository).existsById(STUDENT_INVOICE_ID);
        verify(studentInvoiceRepository, never()).deleteById(STUDENT_INVOICE_ID);
    }

    @Test
    void whenExist_thenReturnTrue() {
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(true);

        studentInvoiceService.isExists(STUDENT_INVOICE_ID);

        verify(studentInvoiceRepository).existsById(STUDENT_INVOICE_ID);
    }

    @Test
    void whenGetUnpaidInvoices_thenReturnList() {
        when(contractService.findAllByStatusOpenAndWait()).thenReturn(Collections.singletonList(contract));

        List<StudentInvoiceDto> result = studentInvoiceService.getUnpaidInvoices();

        assertEquals(Collections.singletonList(studentInvoiceDto), result);
    }

    @Test
    void whenGenerateStudentInvoices_thenReturnList() {
        contract.setStudentInvoices(Collections.emptyList());
        periodDate.setStartDate(LocalDate.parse("2050-05-10"));
        periodDate.setEndDate(LocalDate.parse("2050-06-05"));
        StudentInvoice studentInvoice = new StudentInvoice();
        studentInvoice.setType(StudentInvoiceType.WAIT);
        studentInvoice.setPeriod(periodDate);
        studentInvoice.setMoney(429);
        List<StudentInvoice> invoiceList = studentInvoiceService.generateStudentInvoices(contract);

        assertEquals(Collections.singletonList(studentInvoice2), invoiceList);
    }

    @Test
    void whenPayment_thenReturnTrue() {
        when(studentInvoiceRepository.findById(STUDENT_INVOICE_ID)).thenReturn(Optional.ofNullable(studentInvoice));
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(true);
        when(studentInvoiceRepository.saveAndFlush(studentInvoice)).thenReturn(studentInvoice);
        boolean result = studentInvoiceService.payment(STUDENT_INVOICE_ID);

        assertEquals(true, result);
    }

    @Test
    void whenPayment_thenReturnExceptionNotFound() {
        when(studentInvoiceRepository.findById(STUDENT_INVOICE_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            boolean result = studentInvoiceService.payment(STUDENT_INVOICE_ID);

        });

    }

    @Test
    void whenGetAllOpenAndWaitByPhone_thenReturnList() {
        when(studentService.findStudentByPhone(PHONE)).thenReturn(Optional.ofNullable(student));
        when(contractService.findByStudentAndStatusOpenOrWait(student)).thenReturn(Optional.ofNullable(contract));

        List<StudentInvoiceDto> invoices = studentInvoiceService.getAllOpenAndWaitByPhone(PHONE);

        assertEquals(Collections.singletonList(studentInvoiceDto), invoices);
    }

    @Test
    void whenGetAllOpenAndWaitByPhone_thenReturnStudentException() {
        when(studentService.findStudentByPhone(PHONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            studentInvoiceService.getAllOpenAndWaitByPhone(PHONE);
        });
    }

    @Test
    void whenGetAllOpenAndWaitByPhone_thenReturnContractException() {
        when(studentService.findStudentByPhone(PHONE)).thenReturn(Optional.ofNullable(student));
        when(contractService.findByStudentAndStatusOpenOrWait(student)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            studentInvoiceService.getAllOpenAndWaitByPhone(PHONE);
        });
    }

    @Test
    void whenPayment_thenReturnExceptionUpdate() {
        when(studentInvoiceRepository.findById(STUDENT_INVOICE_ID)).thenReturn(Optional.ofNullable(studentInvoice));
        when(studentInvoiceRepository.existsById(STUDENT_INVOICE_ID)).thenReturn(false);
        assertThrows(UpdateEntityException.class, () -> {
            boolean result = studentInvoiceService.payment(STUDENT_INVOICE_ID);

        });

    }
}
