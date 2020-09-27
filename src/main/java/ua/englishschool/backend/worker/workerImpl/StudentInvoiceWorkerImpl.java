package ua.englishschool.backend.worker.workerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.entity.core.StudentInvoiceType;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.StudentInvoiceService;
import ua.englishschool.backend.worker.StudentInvoiceWorker;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class StudentInvoiceWorkerImpl implements StudentInvoiceWorker {

    @Autowired
    private StudentInvoiceService studentInvoiceService;

    @Autowired
    private ContractService contractService;

    @Override
    @Scheduled(cron = "${generate.invoices}")
    public void generateInvoice() {
        List<Contract> contracts = contractService.findAllByStatusOpenAndWait();
        for (Contract contract : contracts) {
            for (StudentInvoice studentInvoice : contract.getStudentInvoices()) {
                if (studentInvoice.getType() == StudentInvoiceType.OPEN &&
                        studentInvoice.getPeriod().getStartDate().isBefore(LocalDate.now().plusDays(3))) {
                    studentInvoice.setType(StudentInvoiceType.WAIT);
                    studentInvoiceService.update(studentInvoice);
                }
            }
        }
    }

}
