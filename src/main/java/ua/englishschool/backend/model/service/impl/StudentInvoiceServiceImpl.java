package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.core.StudentInvoiceType;
import ua.englishschool.backend.entity.dto.StudentInvoiceDto;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.repository.StudentInvoiceRepository;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.StudentInvoiceService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentInvoiceServiceImpl implements StudentInvoiceService {

    private StudentInvoiceRepository studentInvoiceRepository;

    private ContractService contractService;

    @Autowired
    public StudentInvoiceServiceImpl(StudentInvoiceRepository studentInvoiceRepository, ContractService contractService) {
        this.studentInvoiceRepository = studentInvoiceRepository;
        this.contractService = contractService;
    }

    @Override
    public StudentInvoice create(StudentInvoice studentInvoice) {
        if (studentInvoice.getId() == 0) {
            return studentInvoiceRepository.saveAndFlush(studentInvoice);
        }
        return studentInvoice;
    }

    @Override
    public boolean update(StudentInvoice studentInvoice) {
        if (isExists(studentInvoice.getId())) {
            studentInvoiceRepository.saveAndFlush(studentInvoice);
            return true;
        }
        return false;
    }

    @Override
    public List<StudentInvoice> getAll() {
        return studentInvoiceRepository.findAll();
    }

    @Override
    public Optional<StudentInvoice> getById(long id) {
        return studentInvoiceRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            studentInvoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return studentInvoiceRepository.existsById(id);
    }

    @Override
    public List<StudentInvoiceDto> getUnpaidInvoices() {
        List<Contract> contractList = contractService.findAllByStatusOpenAndWait();
        List<StudentInvoiceDto> unpaidStudentInvoices = new ArrayList<>();
        for (Contract contract : contractList) {
            for (StudentInvoice studentInvoice : contract.getStudentInvoices()) {
                if (studentInvoice.getType() == StudentInvoiceType.WAIT) {
                    unpaidStudentInvoices.add(generateStudentInvoiceDto(contract, studentInvoice));
                }
            }

        }
        return unpaidStudentInvoices;
    }

    @Override
    public List<StudentInvoice> generateStudentInvoices(Contract contract) {
        List<StudentInvoice> invoiceList = new ArrayList<>();
        long allDaysForPayment = getAllPaymentDays(contract);
        long daysToNextPayment = allDaysForPayment % 30;
        invoiceList.add(generateFirstStudentInvoice(contract, daysToNextPayment));
        StudentInvoice studentInvoice;
        PeriodDate periodInvoice;
        for (int i = 0; i < allDaysForPayment / 30; i++) {
            periodInvoice = new PeriodDate();
            studentInvoice = new StudentInvoice();
            studentInvoice.setMoney(contract.getCourse().getPrice());
            periodInvoice.setStartDate(invoiceList.get(i).getPeriod().getEndDate());
            periodInvoice.setEndDate(invoiceList.get(i).getPeriod().getEndDate().plusDays(30));
            studentInvoice.setPeriod(periodInvoice);
            studentInvoice.setType(StudentInvoiceType.OPEN);
            invoiceList.add(studentInvoice);
        }
        return invoiceList;
    }

    @Override
    public boolean payment(long invoiceId) {
        Optional<StudentInvoice> studentInvoice = getById(invoiceId);
        if (studentInvoice.isEmpty()) {
            throw new EntityNotFoundException("Invoice was not found with id: " + invoiceId);
        }
        studentInvoice.get().setType(StudentInvoiceType.CLOSED);
        studentInvoice.get().setPaymentDate(LocalDate.now());
        if (!update(studentInvoice.get())) {
            throw new UpdateEntityException("Invoice was not updated with id: " + invoiceId);
        }
        return true;
    }

    private StudentInvoice generateFirstStudentInvoice(Contract contract, long daysToNextPayment) {
        StudentInvoice studentInvoice = new StudentInvoice();
        PeriodDate periodInvoice = new PeriodDate();
        if (contract.getContractStatusType() == ContractStatusType.OPEN) {
            studentInvoice.setMoney((int) (contract.getCourse().getPrice() / 30 * daysToNextPayment));
            periodInvoice.setStartDate(LocalDate.now());
            periodInvoice.setEndDate(LocalDate.now().plusDays(daysToNextPayment));
            studentInvoice.setPeriod(periodInvoice);
        } else {
            studentInvoice.setMoney((int) (contract.getCourse().getPrice() / 30 * daysToNextPayment));
            periodInvoice.setStartDate(contract.getCourse().getPeriodDate().getStartDate());
            periodInvoice.setEndDate(contract.getCourse().getPeriodDate().getStartDate().plusDays(daysToNextPayment));
            studentInvoice.setPeriod(periodInvoice);
        }
        studentInvoice.setType(StudentInvoiceType.WAIT);
        return studentInvoice;
    }

    private long getAllPaymentDays(Contract contract) {
        if (contract.getCourse().getPeriodDate().getStartDate().isAfter(LocalDate.now())) {
            return contract.getCourse().getPeriodDate().getEndDate().toEpochDay() - contract.getCourse().getPeriodDate().getStartDate().toEpochDay();
        }
        return contract.getCourse().getPeriodDate().getEndDate().toEpochDay() - LocalDate.now().toEpochDay();

    }

    private StudentInvoiceDto generateStudentInvoiceDto(Contract contract, StudentInvoice studentInvoice) {
        StudentInvoiceDto studentInvoiceDto = new StudentInvoiceDto();
        studentInvoiceDto.setInvoiceId(studentInvoice.getId());
        studentInvoiceDto.setFirstName(contract.getStudent().getFirstName());
        studentInvoiceDto.setLastName(contract.getStudent().getLastName());
        studentInvoiceDto.setCourseName(contract.getCourse().getName());
        studentInvoiceDto.setMoney(studentInvoice.getMoney());
        studentInvoiceDto.setPhone(contract.getStudent().getPhoneNumber());
        studentInvoiceDto.setStartDate(studentInvoice.getPeriod().getStartDate());
        studentInvoiceDto.setEndDate(studentInvoice.getPeriod().getEndDate());
        return studentInvoiceDto;
    }
}
