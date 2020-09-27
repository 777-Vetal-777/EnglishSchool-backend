package ua.englishschool.backend.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.entity.dto.StudentInvoiceDto;
import ua.englishschool.backend.model.service.StudentInvoiceService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentInvoiceController.class)
public class StudentInvoiceControllerTest {

    private static final String URL = "/invoices";

    private static final String URL_WAIT_INVOICES = URL + "/wait";

    private static final String URL_PAYMENT = URL + "/payment/{invoiceId}";

    private static final String URL_UNPAID_BY_PHONE = URL + "/unpaid-by-phone/{phone}";

    @Autowired
    private MockMvc server;

    @MockBean
    private StudentInvoiceService studentInvoiceService;

    private List<StudentInvoiceDto> invoiceList;

    private StudentInvoiceDto studentInvoiceDto;

    @BeforeEach
    void setUp() {
        studentInvoiceDto = new StudentInvoiceDto();
        studentInvoiceDto.setMoney(10000);

        invoiceList = new ArrayList<>();
        invoiceList.add(studentInvoiceDto);

    }

    @Test
    void getUnpaidInvoices_ReturnList() throws Exception {
        when(studentInvoiceService.getUnpaidInvoices()).thenReturn(invoiceList);

        server.perform(get(URL_WAIT_INVOICES))
                .andDo(print())
                .andExpect(content().json(asJsonString(invoiceList)))
                .andExpect(status().isOk());
    }

    @Test
    void payment_ReturnTrue() throws Exception {
        when(studentInvoiceService.payment(10)).thenReturn(true);

        server.perform(put(URL_PAYMENT, 10))
                .andDo(print())
                .andExpect(content().string(String.valueOf(true)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUnpaidByPhone_ReturnList() throws Exception {
        when(studentInvoiceService.getAllOpenAndWaitByPhone("12345")).thenReturn(Collections.singletonList(studentInvoiceDto));

        server.perform(get(URL_UNPAID_BY_PHONE, "12345"))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(studentInvoiceDto))))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
