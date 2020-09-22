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
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.model.service.ContractService;

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

    private static final long CONTRACT_ID = 1;

    @Autowired
    private MockMvc server;

    @MockBean
    private ContractService contractService;

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(CONTRACT_ID);
        contract.setContractStatusType(ContractStatusType.OPEN);
    }

    @Test
    void create_ReturnId() throws Exception {
        when(contractService.create(contract)).thenReturn(contract);

        server.perform(post(URL).content(asJsonString(contract)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(String.valueOf(CONTRACT_ID)))
                .andExpect(status().isCreated());

        verify(contractService).create(contract);
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

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
