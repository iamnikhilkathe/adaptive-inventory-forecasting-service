package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.User;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions.UserAuthenticationException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.JwtResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.LoginRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.RegisterRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.UserRepository;

@Service
public class AuthService {
	
	   private final AuthenticationManager authenticationManager;
	    private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	  
	    public AuthService(
	            AuthenticationManager authenticationManager,
	            UserRepository userRepository,
	            PasswordEncoder passwordEncoder) {
	        this.authenticationManager = authenticationManager;
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	
    
	public JwtResponse login(LoginRequest loginRequest) {
		String token;
		 try {
			 Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getUsername(),
	                        loginRequest.getPassword()
	                )
	            );
			 UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			 token = JwtUtilService.generateToken(userDetails);
		        
	        } catch (Exception e) {
	        	
	        	e.printStackTrace();
	            throw new UserAuthenticationException("Invalid username or password",HttpStatus.UNAUTHORIZED);
	        }
	     	 
	        return new JwtResponse(token);
	}
	
	
    
    public User register(RegisterRequest request) {
       
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new UserAuthenticationException("Username already taken" ,HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAuthenticationException("Email already registered",HttpStatus.CONFLICT);
        }
        
        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setDate(LocalDateTime.now());
        
        return userRepository.save(user);
    }



   
  
}
