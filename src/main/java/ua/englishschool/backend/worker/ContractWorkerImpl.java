package ua.englishschool.backend.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.model.service.ContractService;

import java.time.LocalDate;
import java.util.List;

@Component
public class ContractWorkerImpl implements ContractWorker {

    @Autowired
    private ContractService contractService;

    @Override
    @Scheduled(cron = "${close.contracts}")
    public void closeContracts() {
        System.out.println("go");
        List<Contract> contractList = contractService.findAllByEndDateBefore(LocalDate.now());
        for (Contract contract : contractList) {
            contract.setContractStatusType(ContractStatusType.CLOSED);
            contractService.update(contract);
        }
    }
}
