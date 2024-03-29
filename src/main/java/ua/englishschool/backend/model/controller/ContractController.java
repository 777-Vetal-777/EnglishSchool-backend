package ua.englishschool.backend.model.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.ContractDto;
import ua.englishschool.backend.entity.dto.CreateContractDto;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.service.ContractService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ContractController {

    private static final String URL = "/contracts";

    private static final String URL_GET_BY_ID = URL + "/{id}";

    private static final String URL_CREATE_CONTRACT_DTO = URL + "/create-contract";

    private static final String URL_DELETE_BY_ID = URL + "/{id}";

    private static final String URL_GET_ALL_STATUS_OPEN = URL + "/active";

    private static final String URL_FIND_BY_PHONE = URL + "/find-by-phone/{phone}";

    private ContractService contractService;

    private Logger logger;

    @Autowired
    public ContractController(ContractService contractService, Logger logger) {
        this.contractService = contractService;
        this.logger = logger;
    }

    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long createContract(@RequestBody Contract contract) {
        long id = contractService.create(contract).getId();
        logger.debug("Contract with id " + id + " was created successfully");
        return id;
    }

    @PostMapping(URL_CREATE_CONTRACT_DTO)
    @ResponseStatus(HttpStatus.CREATED)
    public long createContract(@RequestBody CreateContractDto createContractDto) {
        return contractService.createContract(createContractDto);
    }

    @GetMapping(URL_GET_BY_ID)
    public Contract getById(@PathVariable("id") long id) {
        return contractService.getById(id).orElseThrow(() -> new EntityNotFoundException("Contract was not found with id: " + id));
    }

    @GetMapping(URL)
    public List<Contract> getAll() {
        return contractService.getAll();
    }

    @DeleteMapping(URL_DELETE_BY_ID)
    public void deleteById(@PathVariable("id") long id) {
        if (!contractService.delete(id)) {
            throw new EntityNotFoundException("Contract was not deleted with id: " + id);
        }
    }

    @PutMapping(URL)
    public void update(@RequestBody Contract contract) {
        if (!contractService.update(contract)) {
            throw new UpdateEntityException("Contract was not updated with id: " + contract.getId());
        }
    }

    @GetMapping(URL_GET_ALL_STATUS_OPEN)
    public List<Contract> getAllStatusOpen() {
        return contractService.getAllByStatus(ContractStatusType.OPEN);
    }

    @GetMapping(URL_FIND_BY_PHONE)
    public ContractDto findByPhone(@PathVariable("phone") String phone) {

        return contractService.findByPhone(phone);
    }
}
