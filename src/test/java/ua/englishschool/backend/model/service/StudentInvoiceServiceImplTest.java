package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.model.repository.StudentInvoiceRepository;
import ua.englishschool.backend.model.service.impl.StudentInvoiceServiceImpl;

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
public class StudentInvoiceServiceImplTest {

    private static final long STUDENT_INVOICE_ID = 1;

    @InjectMocks
    private StudentInvoiceServiceImpl studentInvoiceService;

    @Mock
    private StudentInvoiceRepository studentInvoiceRepository;

    private StudentInvoice studentInvoice;

    @BeforeEach
    void setUp() {
        studentInvoice = new StudentInvoice();
        studentInvoice.setId(STUDENT_INVOICE_ID);
        studentInvoice.setMoney(10000);

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
}
