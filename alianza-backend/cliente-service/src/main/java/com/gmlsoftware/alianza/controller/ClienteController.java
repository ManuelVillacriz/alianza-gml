package com.gmlsoftware.alianza.controller;


import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.gmlsoftware.alianza.common.controller.CommonController;
import com.gmlsoftware.alianza.entity.Cliente;
import com.gmlsoftware.alianza.service.ClienteService;

@RestController
@RequestMapping(value = "/api/clientes")
@CrossOrigin("*")
public class ClienteController extends CommonController<Cliente, ClienteService> {
	
	@PutMapping("/{id}")
	public ResponseEntity<?> edit(@Validated @RequestBody Cliente cliente, BindingResult result, @PathVariable(name = "id") Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Cliente> objeto = service.findById(id);
		
		if(objeto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Cliente clienteDb = objeto.get();
		clienteDb.setName(cliente.getName());
		clienteDb.setLastName(cliente.getLastName());
		
		clienteDb.setEmail(cliente.getEmail());
		clienteDb.setPhone(cliente.getPhone());		
		clienteDb.setStartDate(cliente.getStartDate());
		clienteDb.setEndDate(cliente.getEndDate());
		clienteDb.setEstado(cliente.isEstado());
						
		return  ResponseEntity.status(HttpStatus.CREATED).body(service.save(clienteDb));
	}	

	
	@GetMapping("/search")
	public ResponseEntity<?> advancedSearch(
	        @RequestParam(required = false) String sharedKey,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String lastName,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String phone,
	        Pageable pageable) {
	    return ResponseEntity.ok(service.advancedSearch(sharedKey, name, lastName, email, phone, pageable));
	}

	
	 @GetMapping("/export")
	    public ResponseEntity<String> exportClients() {
	        String csvData = service.generateCsv();

	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientes.csv")
	            .contentType(MediaType.parseMediaType("text/csv"))
	            .body(csvData);
	    }
	

}
