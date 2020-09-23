package ua.englishschool.backend.model.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ContractRepositoryTest {

    private static final String PHONE = "777777777";

    private Contract contract;

    private Contract contract2;

    private Course course;

    private Student student;

    private Student student2;

    private Course course2;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {

        course = new Course();
        course.setId(0);
        course.setName("ELEMENTARY");
        course.setMaxCapacity(20);

        entityManager.persistAndFlush(course);

        course2 = new Course();
        course2.setId(0);
        course2.setName("ELEMENTARY");
        course2.setMaxCapacity(20);
        entityManager.persistAndFlush(course2);


        student = new Student();
        student.setId(0);
        student.setFirstName("firstName");
        student.setLastName("lastName");
        student.setPhoneNumber(PHONE);

        entityManager.persistAndFlush(student);

        student2 = new Student();
        student2.setId(0);
        student2.setFirstName("firstName");
        student2.setLastName("lastName");
        student2.setPhoneNumber("12345");

        entityManager.persistAndFlush(student2);


        contract = new Contract();
        contract.setId(0);
        contract.setCourse(course);
        contract.setContractStatusType(ContractStatusType.OPEN);
        contract.setStudent(student);

        entityManager.persistAndFlush(contract);

        contract2 = new Contract();
        contract2.setId(0);
        contract2.setCourse(course);
        contract2.setContractStatusType(ContractStatusType.OPEN);
        contract2.setStudent(student2);

        entityManager.persistAndFlush(contract2);
    }

    @Test
    void whenFindAllByEndDateBefore_ReturnList() {
        PeriodDate periodDate = new PeriodDate();
        periodDate.setEndDate(LocalDate.parse("2020-09-22"));
        course2.setPeriodDate(periodDate);
        contract2.setCourse(course2);
        entityManager.merge(contract2);


        List<Contract> result = contractRepository.findAllByEndDateBefore(LocalDate.now());

        assertEquals(Collections.singletonList(contract2), result);
    }

    @Test
    void whenFindCountByStatusOpenAndCourse_thenReturnCount2() {
        int count = contractRepository.findCountByCourseAndStatus(course, ContractStatusType.OPEN);
        assertEquals(2, count);
    }

    @Test
    void whenFindCountByStatusOpenAndCourse_thenReturnCount1() {
        contract2.setContractStatusType(ContractStatusType.CLOSED);
        entityManager.merge(contract2);
        int count = contractRepository.findCountByCourseAndStatus(course, ContractStatusType.OPEN);
        assertEquals(1, count);
    }

    @Test
    void whenFindCountByStatusOpenAndWaitAndCourse_thenReturnCountOpens() {

        int count = contractRepository.findCountByStatusOpenAndWaitAndCourse(course);

        assertEquals(2, count);
    }

    @Test
    void whenFindCountByStatusOpenAndWaitAndCourse_thenReturnCountOpenWait() {
        contract2.setContractStatusType(ContractStatusType.WAIT);
        entityManager.merge(contract2);
        int count = contractRepository.findCountByStatusOpenAndWaitAndCourse(course);

        assertEquals(2, count);
    }

    @Test
    void whenFindCountByStatusOpenAndWaitAndCourse_thenReturnCountOpen() {
        contract2.setContractStatusType(ContractStatusType.CLOSED);
        entityManager.merge(contract2);
        int count = contractRepository.findCountByStatusOpenAndWaitAndCourse(course);

        assertEquals(1, count);
    }


}
