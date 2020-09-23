package ua.englishschool.backend.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.ContractDto;
import ua.englishschool.backend.entity.dto.CreateContractDto;
import ua.englishschool.backend.model.service.ContractService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContractController.class)
@MockBean(Logger.class)
public class ContractControllerTest {

    private static final String URL = "/contracts";

    private static final String GET_BY_ID = URL + "/{id}";

    private static final String DELETE_BY_ID = URL + "/{id}";

    private static final String GET_ALL_WITH_STATUS_OPEN = URL + "/active";

    private static final String URL_CREATE_CONTRACT_DTO = URL + "/create-contract";

    private static final String URL_FIND_BY_PHONE = URL + "/find-by-phone/{phone}";

    private static final String PHONE = "12345";

    private static final long CONTRACT_ID = 1;

    @Autowired
    private MockMvc server;

    @MockBean
    private ContractService contractService;

    private Contract contract;

    private CreateContractDto createContractDto;

    private ContractDto contractDto;

    @BeforeEach
    void setUp() {

        contract = new Contract();
        contract.setId(CONTRACT_ID);
        contract.setContractStatusType(ContractStatusType.OPEN);

        createContractDto = new CreateContractDto();
        createContractDto.setCourseId(1);
        createContractDto.setCourseId(2);

        contractDto = new ContractDto();
        contractDto.setPhone(PHONE);
    }


    @Test
    void getById_ReturnContract() throws Exception {
        when(contractService.getById(CONTRACT_ID)).thenReturn(Optional.ofNullable(contract));

        server.perform(get(GET_BY_ID, String.valueOf(CONTRACT_ID)))
                .andDo(print())
                .andExpect(content().json(asJsonString(contract)))
                .andExpect(status().isOk());

        verify(contractService).getById(CONTRACT_ID);
    }

    @Test
    void getById_IfNotFoundContract() throws Exception {
        when(contractService.getById(CONTRACT_ID)).thenReturn(Optional.empty());

        server.perform(get(GET_BY_ID, String.valueOf(CONTRACT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(contractService).getById(CONTRACT_ID);
    }

    @Test
    void getAll_ReturnList() throws Exception {
        when(contractService.getAll()).thenReturn(Collections.singletonList(contract));

        server.perform(get(URL))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singleton(contract))))
                .andExpect(status().isOk());

        verify(contractService).getAll();
    }

    @Test
    void create_ReturnId() throws Exception {
        PeriodDate periodDate = new PeriodDate();
        periodDate.setStartDate(LocalDate.now());
        Course course = new Course();
        course.setPeriodDate(periodDate);
        contract.setCourse(course);

        when(contractService.create(contract)).thenReturn(contract);

        server.perform(post(URL).content(asJsonString(contract)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(String.valueOf(CONTRACT_ID)))
                .andExpect(status().isCreated());

        verify(contractService).create(contract);
    }

    @Test
    void createContractCreateContractDto_ReturnId() throws Exception {
        when(contractService.createContract(createContractDto)).thenReturn(Long.valueOf(3));

        server.perform(post(URL_CREATE_CONTRACT_DTO).content(asJsonString(createContractDto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(String.valueOf(3)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteById_ReturnOK() throws Exception {
        when(contractService.delete(CONTRACT_ID)).thenReturn(true);

        server.perform(delete(DELETE_BY_ID, String.valueOf(CONTRACT_ID)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(contractService).delete(CONTRACT_ID);
    }

    @Test
    void deleteById_IfNotFoundContract() throws Exception {
        when(contractService.delete(CONTRACT_ID)).thenReturn(false);

        server.perform(delete(DELETE_BY_ID, String.valueOf(CONTRACT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(contractService).delete(CONTRACT_ID);
    }

    @Test
    void update_ReturnOK() throws Exception {
        when(contractService.update(contract)).thenReturn(true);

        server.perform(put(URL).content(asJsonString(contract)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(contractService).update(contract);
    }

    @Test
    void update_IfNotUpdatedContract() throws Exception {
        when(contractService.update(contract)).thenReturn(false);

        server.perform(put(URL).content(asJsonString(contract)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(contractService).update(contract);
    }

    @Test
    void getAllStatusOpen_ReturnList() throws Exception {
        when(contractService.getAllByStatus(ContractStatusType.OPEN)).thenReturn(Collections.singletonList(contract));

        server.perform(get(GET_ALL_WITH_STATUS_OPEN))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(contract))))
                .andExpect(status().isOk());

        verify(contractService).getAllByStatus(ContractStatusType.OPEN);
    }

    @Test
    void findByPhone_ReturnContractDto() throws Exception {
        when(contractService.findByPhone(PHONE)).thenReturn(contractDto);

        server.perform(get(URL_FIND_BY_PHONE, PHONE))
                .andDo(print())
                .andExpect(content().json(asJsonString(contractDto)))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
