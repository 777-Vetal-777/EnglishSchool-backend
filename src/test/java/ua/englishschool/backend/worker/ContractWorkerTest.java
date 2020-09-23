package ua.englishschool.backend.worker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.model.service.ContractService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractWorkerTest {

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractWorkerImpl contractWorkerImpl;

    private Contract contract;

    private Contract contract2;

    private Course course;

    private Course course2;

    private PeriodDate periodDate;

    private PeriodDate periodDate2;

    @BeforeEach
    void setUp() {
        periodDate = new PeriodDate();
        periodDate.setEndDate(LocalDate.parse("2020-09-22"));
        periodDate2 = new PeriodDate();
        periodDate2.setEndDate(LocalDate.parse("2020-09-20"));


        course = new Course();
        course.setPeriodDate(periodDate);
        course2 = new Course();
        course2.setPeriodDate(periodDate2);

        contract = new Contract();
        contract.setCourse(course);
        contract.setContractStatusType(ContractStatusType.WAIT);

        contract2 = new Contract();
        contract2.setCourse(course2);
        contract2.setContractStatusType(ContractStatusType.OPEN);

    }

    @Test
    void whenCloseContracts_thenReturnClosed() {

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        when(contractService.findAllByEndDateBefore(LocalDate.now())).thenReturn(Arrays.asList(contract, contract2));
        when(contractService.update(any(Contract.class))).thenReturn(true);

        contractWorkerImpl.closeContracts();
        verify(contractService, times(2)).update(captor.capture());
        List<Contract> contractList = captor.getAllValues();
        for (Contract contract : contractList) {
            assertEquals(ContractStatusType.CLOSED, contract.getContractStatusType());
        }
    }

}
