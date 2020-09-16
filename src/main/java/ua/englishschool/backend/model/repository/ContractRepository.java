package ua.englishschool.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.core.ContractStatusType;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {


    List<Contract> findAllByContractStatusType(ContractStatusType statusType);

    List<Contract> findAllByCourseAndContractStatusType(Course course, ContractStatusType statusType);
}
