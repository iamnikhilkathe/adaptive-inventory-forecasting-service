package com.nikhil.inventory.adaptiveinventoryforecastingservice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, nullable = false, length = 50)
	private String userName;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false,length = 100,unique = true)
	private String email;
	
	
	private boolean isActive;
	
	private LocalDateTime date;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<UserRoles> role;


	
	
	
	

}
