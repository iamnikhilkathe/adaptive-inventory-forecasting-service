package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ParseRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ParseResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.LlmParseService;

@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class LlmParseController {

    private final LlmParseService llmParseService;

    
    @PostMapping("/parse")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ParseResponse> parse(@Valid @RequestBody ParseRequest request) {
        ParseResponse resp = llmParseService.parse(request);
        return ResponseEntity.ok(resp);
    }
}
