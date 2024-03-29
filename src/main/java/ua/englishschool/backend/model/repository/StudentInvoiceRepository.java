package ua.englishschool.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.englishschool.backend.entity.StudentInvoice;

import java.util.List;

@Repository
public interface StudentInvoiceRepository extends JpaRepository<StudentInvoice, Long> {
}
