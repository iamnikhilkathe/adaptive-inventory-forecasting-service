package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class InMemoryCacheService {

	private final Set<String> blacklistJwtTokenList = new HashSet<>();

	
	  public void blacklistToken(String token) {
		  blacklistJwtTokenList.add(token);
	    }

	    public boolean isTokenBlacklisted(String token) {
	        return blacklistJwtTokenList.contains(token);
	    }

	
}
