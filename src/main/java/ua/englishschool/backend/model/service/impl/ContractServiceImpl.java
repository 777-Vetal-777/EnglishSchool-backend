package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.model.repository.ContractRepository;
import ua.englishschool.backend.model.service.ContractService;

import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Contract create(Contract contract) {
        if (contract.getId() == 0) {
            return contractRepository.saveAndFlush(contract);
        }
        return contract;
    }

    @Override
    public boolean update(Contract contract) {
        if (isExists(contract.getId())) {
            contractRepository.saveAndFlush(contract);
            return true;
        }
        return false;
    }

    @Override
    public List<Contract> getAll() {
        return contractRepository.findAll();
    }

    @Override
    public Optional<Contract> getById(long id) {
        return contractRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            contractRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return contractRepository.existsById(id);
    }

    @Override
    public List<Contract> getAllByStatus(ContractStatusType statusType) {
        return contractRepository.findAllByContractStatusType(statusType);

    }

    @Override
    public List<Contract> getAllByCourseAndStatusType(Course course, ContractStatusType statusType) {
        return contractRepository.findAllByCourseAndContractStatusType(course, statusType);
    }
}
