package com.nikhil.inventory.adaptiveinventoryforecastingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {

//	 @Bean("openAiClient")
//    public WebClient openAiClient( @Value("${llm.openai.api-key}") String apiKey,
//    							   @Value("${llm.openai.base-url}") String baseUrl) {
//        return WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
	
	 @Bean("groqClient")
	    public WebClient groqClient(@Value("${groq.api.key}") String groqApiKey) {
	        return WebClient.builder()
	                .baseUrl("https://api.groq.com")  // or whatever the relevant endpoint is
	                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + groqApiKey)
	                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	                .build();
	    }
}