package com.gmlsoftware.alianza.service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmlsoftware.alianza.common.service.CommonServiceImpl;
import com.gmlsoftware.alianza.entity.Cliente;
import com.gmlsoftware.alianza.exception.ClientExistException;
import com.gmlsoftware.alianza.exception.DateException;
import com.gmlsoftware.alianza.repository.ClienteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClienteServiceImpl extends CommonServiceImpl<Cliente, ClienteRepository> implements ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);


    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Cliente save(Cliente entity) {
        logger.info("Iniciando el procesamiento del cliente con correo: {}", entity.getEmail());
        
        try {
            validateClient(entity);
            generateSharedKey(entity);

            Cliente savedClient = repository.save(entity);
            logger.info("Cliente con correo: {} procesado y guardado exitosamente con ID: {}", entity.getEmail(), savedClient.getId());
            return savedClient;

        } catch (ClientExistException | DateException e) {
            logger.error("Error en el procesamiento del cliente con correo: {}: {}", entity.getEmail(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al guardar el cliente: ", e);
            throw e;
        }
    }

    private void validateClient(Cliente entity) {
        logger.debug("Validando cliente con correo: {}", entity.getEmail());
        
        if (entity.getId() == null && repository.findByEmail(entity.getEmail()).isPresent()) {
            logger.warn("El cliente con correo: {} ya está registrado.", entity.getEmail());
            throw new ClientExistException("El cliente con el correo " + entity.getEmail() + " ya se encuentra registrado.");
        }

        if (entity.getEndDate() != null && entity.getStartDate() != null) {
            if (!entity.getEndDate().isAfter(entity.getStartDate())) {
                logger.warn("Las fechas de inicio y fin no son válidas para el cliente con correo: {}", entity.getEmail());
                throw new DateException("La fecha de fin debe ser posterior a la fecha de inicio.");
            }
        }
    }

    private void generateSharedKey(Cliente entity) {
        logger.debug("Generando sharedKey para el cliente con correo: {}", entity.getEmail());
        String sharedKey = (entity.getName().charAt(0) + entity.getLastName()).toLowerCase();
        entity.setSharedKey(sharedKey);
        logger.debug("SharedKey generado: {}", sharedKey);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> advancedSearch(String sharedKey, String name, String lastName, String email, String phone, Pageable pageable) {
        logger.info("Realizando búsqueda avanzada con filtros: sharedKey={}, name={}, lastName={}, email={}, phone={}",
                sharedKey, name, lastName, email, phone);
        return repository.advancedSearch(sharedKey, name, lastName, email, phone, pageable);
    }


    public String generateCsv() {
        logger.info("Generando CSV para todos los clientes.");

        List<Cliente> clientes = fetchClientes(); 
        CSVFormat csvFormat = createCsvFormat(); 

        try {
            return writeCsv(clientes, csvFormat); 
        } catch (IOException e) {
            logger.error("Error al generar el CSV: ", e);
            return "";
        }
    }

    private List<Cliente> fetchClientes() {
        logger.debug("Obteniendo la lista de clientes del repositorio.");
        return repository.findAll();
    }

    private CSVFormat createCsvFormat() {
        logger.debug("Configurando el formato del CSV.");
        return CSVFormat.DEFAULT.builder()
                .setHeader("Shared Key", "Name", "LastName", "E-Mail", "Phone", "Date Added")
                .build();
    }

    private String writeCsv(List<Cliente> clientes, CSVFormat csvFormat) throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat)) {

            populateCsvData(csvPrinter, clientes); 
            csvPrinter.flush();

            logger.info("CSV generado exitosamente.");
            return stringWriter.toString();
        }
    }

    private void populateCsvData(CSVPrinter csvPrinter, List<Cliente> clientes) throws IOException {
        logger.debug("Agregando registros al CSV.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Cliente cliente : clientes) {
            logger.debug("Agregando registro al CSV para el cliente con ID: {}", cliente.getId());
            csvPrinter.printRecord(
                    cliente.getSharedKey(),
                    cliente.getName(),
                    cliente.getLastName(),
                    cliente.getEmail(),
                    cliente.getPhone(),
                    cliente.getDataAdded().format(formatter)
            );
        }
    }

}
