package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.StudentInvoice;
import ua.englishschool.backend.model.repository.StudentInvoiceRepository;
import ua.englishschool.backend.model.service.StudentInvoiceService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentInvoiceServiceImpl implements StudentInvoiceService {

    @Autowired
    private StudentInvoiceRepository studentInvoiceRepository;

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
}
