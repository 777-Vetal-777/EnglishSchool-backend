package ua.englishschool.backend.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.model.repository.ContractRepository;
import ua.englishschool.backend.model.service.impl.ContractServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    private static final long CONTRACT_ID = 1;

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(CONTRACT_ID);
    }

    @Test
    void whenSave_thenReturnContract() {
        contract.setId(0);
        when(contractRepository.saveAndFlush(contract)).thenReturn(contract);

        contractService.create(contract);

        verify(contractRepository).saveAndFlush(contract);

    }

    @Test
    void whenUpdate_thenReturnTrue() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        boolean result = contractService.update(contract);

        verify(contractRepository).saveAndFlush(contract);
        verify(contractRepository).existsById(CONTRACT_ID);
        assertTrue(result);
    }

    @Test
    void whenUpdate_thenReturnFalse() {
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(false);

        boolean result = contractService.update(contract);

        assertFalse(result);
        verify(contractRepository, never()).saveAndFlush(contract);
        verify(contractRepository).existsById(CONTRACT_ID);

    }

    @Test
    void whenGetAll_thenReturnList() {
        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        List<Contract> result = contractService.getAll();

        assertEquals(Collections.singletonList(contract), result);
    }

    @Test
    void whenGetById_thenReturnContract() {
        when(contractRepository.findById(CONTRACT_ID)).thenReturn(Optional.ofNullable(contract));

        Optional<Contract> result = contractService.getById(CONTRACT_ID);

        assertEquals(contract, result.get());
        verify(contractRepository).findById(CONTRACT_ID);
    }

    @Test
    void whenDelete_thenReturnTrue(){
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        boolean result = contractService.delete(CONTRACT_ID);

        assertTrue(result);
        verify(contractRepository).existsById(CONTRACT_ID);
        verify(contractRepository).deleteById(CONTRACT_ID);
    }

    @Test
    void whenDelete_thenReturnFalse(){
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(false);

        boolean result = contractService.delete(CONTRACT_ID);

        assertFalse(result);
        verify(contractRepository).existsById(CONTRACT_ID);
        verify(contractRepository,never()).deleteById(CONTRACT_ID);
    }

    @Test
    void whenExist_thenReturnTrue(){
        when(contractRepository.existsById(CONTRACT_ID)).thenReturn(true);

        contractService.isExists(CONTRACT_ID);

        verify(contractRepository).existsById(CONTRACT_ID);
    }
}
