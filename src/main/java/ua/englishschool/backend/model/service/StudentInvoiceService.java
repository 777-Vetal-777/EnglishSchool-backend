package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.entity.dto.StudentInvoiceDto;

import java.util.List;

public interface StudentInvoiceService extends GenericService<StudentInvoice> {

    List<StudentInvoiceDto> getWaitInvoices();

    List<StudentInvoice> generateStudentInvoices(Contract contract);

    boolean payment(long invoiceId);

    List<StudentInvoiceDto> getAllOpenAndWaitByPhone(String phone);
}
