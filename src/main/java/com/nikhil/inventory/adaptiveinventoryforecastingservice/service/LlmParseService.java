package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ParseRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ParseResponse;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service

public class LlmParseService {
	
	@Qualifier("groqClient")
    private final WebClient groqClient;
    
    
    public LlmParseService(WebClient groqClient, @Value("${groq.model}")String model) {
		super();
		this.groqClient = groqClient;
		this.model = model;
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

    private  String model;

    public ParseResponse parse(ParseRequest request) {
        String userInput = request.getData();
        String systemPrompt = buildSystemPrompt();

        Map<String, Object> payload = Map.of(
                "model", model, 
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userInput)
                ),
                "temperature", 0
        );

        JsonNode resp = groqClient.post()
                .uri("/openai/v1/chat/completions")
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Groq API error: " + body)))
                .bodyToMono(JsonNode.class)
                .block();
        String assistantText = resp.at("/choices/0/message/content").asText();
        System.out.println(assistantText);
        // Try to parse assistantText as JSON
       
        ParseResponse response = null;
		try {
			 JsonNode parsedJson = new ObjectMapper().readTree(assistantText);
			 System.out.println(parsedJson);
			response = objectMapper.treeToValue(parsedJson, ParseResponse.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response ;
		
    }

    private String buildSystemPrompt() {
        return """
        You are a strict JSON extractor. Given an input sentence, output a single JSON object (and nothing else) with the exact fields:
        - product_name (string or null)
        - category (string or null)
        - quantity (integer or null)
        - sale_date (YYYY-MM-DD or null)
        - location (string or null)
        - confidence (float between 0.0 and 1.0)
        Rules:
        1) Output only valid JSON. NO explanation, no backticks, no extra text.
        2) If a field is missing/ambiguous, set it to null and add text in 'warnings' (array).
        3) check if there is any date od sale convert into yyy-mm-dd
        4) Example:
        { "product_name":"blue office chairs","category":"Furniture","quantity":45,"sale_date":"2025-07-15","location":"West warehouse"}
        """;
    }
    
   
	
    
    
    
    
    
	
}
