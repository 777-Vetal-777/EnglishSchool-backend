package ua.englishschool.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {


    List<Contract> findAllByContractStatusType(ContractStatusType statusType);

    List<Contract> findAllByCourseAndContractStatusType(Course course, ContractStatusType statusType);

    Optional<Contract> findContractByStudentAndContractStatusType(Student student, ContractStatusType contractStatusType);

    @Query("Select Count(c) from Contract c where c.course = :course and c.contractStatusType = 'OPEN' or c.contractStatusType = 'WAIT'")
    int findCountByStatusOpenAndWaitAndCourse(@Param("course") Course course);

    @Query("Select c from Contract c where c.student = :student and c.contractStatusType = 'OPEN' or c.contractStatusType = 'WAIT'")
    Optional<Contract> findByStudentAndStatusTypeOpenOrWait(@Param("student") Student student);

    @Query("Select Count(c) from Contract c where c.course = :course and c.contractStatusType = :type")
    int findCountByCourseAndStatus(@Param("course") Course course, @Param("type") ContractStatusType type);
}
