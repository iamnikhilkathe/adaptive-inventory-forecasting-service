package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions.LlmParsingException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions.UserAuthenticationException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionController {

	
	 @ExceptionHandler(JsonMappingException.class)
	    public ResponseEntity<String> handleJsonMappingException(JsonMappingException ex) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body("JSON mapping error: " + ex.getOriginalMessage());
	    }
	
	 
	  @ExceptionHandler(UserAuthenticationException.class)
	    public ResponseEntity<Map<String, Object>> handleAuthException(UserAuthenticationException ex) {
	        Map<String, Object> error = new HashMap<>();
	        
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status", ex.getHttpStatus()==null ?HttpStatus.UNAUTHORIZED.value() :ex.getHttpStatus() );
	     //   error.put("error", "Unauthorized");
	        error.put("message", ex.getMessage());
	        return new ResponseEntity<>(error, ex.getHttpStatus()==null ? HttpStatus.UNAUTHORIZED : ex.getHttpStatus());
	    }
	  
	  @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult().getAllErrors().forEach(error -> {
	            String fieldName = ((FieldError) error).getField();
	            String errorMessage = error.getDefaultMessage();
	            errors.put(fieldName, errorMessage);
	        });

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }

	  @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                Map.of(
	                        "timestamp", Instant.now().toString(),
	                        "status", HttpStatus.UNAUTHORIZED.value(),
	                        "error", "Unauthorized",
	                        "message", "Authentication required or invalid credentials",
	                        "path", ""
	                )
	        );
	    }
	  
	   @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
	                Map.of(
	                        "timestamp", Instant.now().toString(),
	                        "status", HttpStatus.FORBIDDEN.value(),
	                        "error", "Forbidden",
	                        "message", "You do not have permission to access this resource",
	                        "path", "" // you can populate request path via HttpServletRequest
	                )
	        );
	    }
	   
	   @ExceptionHandler(LlmParsingException.class)
	    public ResponseEntity<Map<String,Object>> handleLlmParsing(LlmParsingException ex) {
	        Map<String,Object> body = Map.of(
	            "error", "ambiguous_data",
	            "message", ex.getMessage(),
	            "ambiguous_fields", ex.getAmbiguousFields()
	        );
	        return ResponseEntity.unprocessableEntity().body(body); // 422
	    }
	   
	   @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
	        Map<String, Object> errorBody = new HashMap<>();
	        errorBody.put("timestamp", LocalDateTime.now());
	        errorBody.put("error", "Method Not Allowed");
	        errorBody.put("message", ex.getMessage());

	        // Convert supported HttpMethod enum to strings
	        Set<String> supportedMethods;
			if (ex.getSupportedHttpMethods() != null)
				supportedMethods = ex.getSupportedHttpMethods()
                .stream()
                .map(method -> method.toString()) // use toString instead of Enum::name
                .collect(Collectors.toSet());
			else
				supportedMethods = Set.of();

	        errorBody.put("supportedMethods", supportedMethods);

	        return new ResponseEntity<>(errorBody, HttpStatus.METHOD_NOT_ALLOWED);
	    }
}
