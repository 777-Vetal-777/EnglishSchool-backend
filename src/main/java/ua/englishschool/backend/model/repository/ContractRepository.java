package ua.englishschool.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.englishschool.backend.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}