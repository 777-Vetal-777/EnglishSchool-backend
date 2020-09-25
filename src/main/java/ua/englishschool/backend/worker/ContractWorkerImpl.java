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

    private ContractService contractService;

    @Autowired
    public ContractWorkerImpl(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    @Scheduled(cron = "${close.contracts}")
    public void closeContracts() {
        List<Contract> contractList = contractService.findAllByEndDateBefore(LocalDate.now());
        for (Contract contract : contractList) {
            contract.setContractStatusType(ContractStatusType.CLOSED);
            contractService.update(contract);
        }
    }

    @Override
    @Scheduled(cron = "${open.contracts}")
    public void openContracts() {
        List<Contract> contractList = contractService.findAllByWaitAndStartDateBefore(LocalDate.now());
        for (Contract contract : contractList) {
            contract.setContractStatusType(ContractStatusType.OPEN);
            contractService.update(contract);
        }
    }
}
