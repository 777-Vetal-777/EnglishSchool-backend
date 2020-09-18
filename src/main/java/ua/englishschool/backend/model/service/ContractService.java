package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;

import java.util.List;
import java.util.Optional;

public interface ContractService extends GenericService<Contract> {

    List<Contract> getAllByStatus(ContractStatusType statusType);

    List<Contract> getAllByCourseAndStatusType(Course course, ContractStatusType statusType);

    Optional<Contract> findContractByStudentAndContractStatusType(Student student, ContractStatusType contractStatusType);
}
