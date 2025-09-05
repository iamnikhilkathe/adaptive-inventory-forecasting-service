package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.User;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.JwtResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.LoginRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.RegisterRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.AuthService;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.InMemoryCacheService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class UserManagementAuthenticationController {
	
	@Autowired
	private InMemoryCacheService cacheService;
	
	@Autowired
	private AuthService authService;
	
	
	    @PostMapping("/register")
	    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		  User user = authService.register(registerRequest);
	        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
	    }

	
	  
	    @PostMapping("/login")
	    public JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
	    	return authService.login(loginRequest);
	    }
	    
	    @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            cacheService.blacklistToken(token);
	            return ResponseEntity.ok("Logged out successfully. Token invalidated.");
	        }
	        return ResponseEntity.badRequest().body("Invalid Authorization header");
	    }
}
