package ua.englishschool.backend.model.service;

import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.core.ContractStatusType;

import java.util.List;

public interface ContractService extends GenericService<Contract> {

    List<Contract> getAllByStatus(ContractStatusType statusType);

    List<Contract> getAllByCourseAndStatusType(Course course, ContractStatusType statusType);
}
