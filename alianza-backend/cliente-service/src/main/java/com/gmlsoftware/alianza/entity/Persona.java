package com.gmlsoftware.alianza.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "El nombre no puede ser nulo.")
	@Size(max = 20, message = "El nombre no puede exceder los 20 caracteres.")
	@NotBlank(message = "El nombre no puede ser vacio.")
	private String name;	
	
	
	@NotNull(message = "El apellido no puede ser nulo.")
	@Size(max = 30, message = "El apellido no puede exceder los 30 caracteres.")
	@NotBlank(message = "El apellido no puede ser vacio.")
	@Column(name = "last_name")
	private String lastName;
		
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genero_id")	
	private Gender gender;	
	
}
