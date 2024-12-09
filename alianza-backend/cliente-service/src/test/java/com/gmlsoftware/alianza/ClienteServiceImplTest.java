package com.gmlsoftware.alianza;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gmlsoftware.alianza.entity.Cliente;
import com.gmlsoftware.alianza.exception.ClientExistException;
import com.gmlsoftware.alianza.exception.DateException;
import com.gmlsoftware.alianza.repository.ClienteRepository;
import com.gmlsoftware.alianza.service.ClienteServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void testSaveClienteSuccessfully() {
        Cliente cliente = new Cliente();
        cliente.setName("Juan");
        cliente.setLastName("Perez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setStartDate(LocalDate.now());
        cliente.setEndDate(LocalDate.now().plusDays(1));

        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente savedCliente = clienteService.save(cliente);

        assertNotNull(savedCliente);
        assertEquals("juanperez", savedCliente.getSharedKey());
        Mockito.verify(clienteRepository).save(cliente);
    }

    @Test
    void testSaveClienteAlreadyExists() {
        Cliente cliente = new Cliente();
        cliente.setName("Juan");
        cliente.setLastName("Perez");
        cliente.setEmail("juan.perez@example.com");

        Mockito.when(clienteRepository.findByEmail("juan.perez@example.com"))
                .thenReturn(Optional.of(new Cliente()));

        assertThrows(ClientExistException.class, () -> clienteService.save(cliente));
        Mockito.verify(clienteRepository, Mockito.never()).save(Mockito.any(Cliente.class));
    }

    @Test
    void testSaveClienteInvalidDates() {
        Cliente cliente = new Cliente();
        cliente.setName("Juan");
        cliente.setLastName("Perez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setStartDate(LocalDate.now());
        cliente.setEndDate(LocalDate.now().minusDays(1));

        assertThrows(DateException.class, () -> clienteService.save(cliente));
        Mockito.verify(clienteRepository, Mockito.never()).save(Mockito.any(Cliente.class));
    }

    @Test
    void testSaveUnexpectedError() {
        Cliente cliente = new Cliente();
        cliente.setName("Juan");
        cliente.setLastName("Perez");
        cliente.setEmail("juan.perez@example.com");

        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> clienteService.save(cliente));
        Mockito.verify(clienteRepository).save(cliente);
    }
}
