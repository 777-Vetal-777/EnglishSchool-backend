package ua.englishschool.backend.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.dto.StudentInvoiceDto;
import ua.englishschool.backend.model.service.StudentInvoiceService;

import java.util.List;

@RestController
public class StudentInvoiceController {

    private static final String URL = "/invoices";

    private static final String URL_UNPAID = URL + "/unpaid";

    @Autowired
    private StudentInvoiceService studentInvoiceService;

    @GetMapping(URL_UNPAID)
    public List<StudentInvoiceDto> getUnpaidInvoices() {
        List<StudentInvoiceDto> studentInvoices = studentInvoiceService.getUnpaidInvoices();
        return studentInvoices;
    }

}
