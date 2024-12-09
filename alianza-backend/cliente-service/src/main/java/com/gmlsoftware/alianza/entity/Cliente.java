package com.gmlsoftware.alianza.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class Cliente extends Persona{
	
	@Column(name = "shared_key")
	private String sharedKey;
	
	@NotNull(message = "El correo electrónico no puede ser nulo.")
	@Email(message = "El formato del correo electrónico es inválido.")
	@Size(max = 30, message = "El correo electronico no puede exceder los 30 caracteres.")
	@NotBlank(message = "El correo electrónico no puede ser vacio.")
	private String email;
	
	@NotNull(message = "El telefono no puede ser nulo.")
	@Size(max = 15, message = "El telefono no puede exceder los 15 caracteres.")
	@NotBlank(message = "El telefono no puede ser vacio.")
	private String phone;
	
	@NotNull(message = "La fecha de inicio no puede ser nulo.")
	@Column(name = "start_date")
	private LocalDate startDate;
	@NotNull(message = "La fecha de fin no puede ser nulo.")
	@Column(name = "end_date")
	private LocalDate endDate;
	
	private LocalDateTime dataAdded;
	private boolean estado;
	

	@PrePersist
    protected void onCreate() {
        this.estado = true;
        this.dataAdded =  LocalDateTime.now(); 
    }
	

}
