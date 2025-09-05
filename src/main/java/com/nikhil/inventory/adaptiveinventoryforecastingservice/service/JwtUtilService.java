package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.MainUserPrincipal;

public class JwtUtilService {
	  private static final String SECRET = "your-256-bit-secret-key-goes-here-your-256-bit-secret"; 
	    private static final long EXPIRATION_TIME = 1000 * 60 * 15; 
	    private static final String ISSUER = "my-app";

	    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

	    // ✅ Generate token with roles
	    public static String generateToken(UserDetails userDetails) {
	        List<String> roles = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.toList());
	        MainUserPrincipal userObj=(MainUserPrincipal) userDetails;
	        Long  userId= userObj.getid();
	        return Jwts.builder()
	                .setSubject(userDetails.getUsername())
	                .claim("roles", roles)
	                .claim("userId", userId) 
	                .setIssuer(ISSUER)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(key, SignatureAlgorithm.HS256)
	                .compact();
	    }

	    // ✅ Validate token and return claims
	    public static Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    public static String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	    }
	    
	    public static List<String> extractRoles(String token) {
	        return extractAllClaims(token).get("roles", List.class);
	    }

	    public static boolean validateToken(String token, String username) {
	        final String tokenusername = extractUsername(token);
	        return (tokenusername.equals(username) && !isTokenExpired(token));
	    }

	    public static boolean isTokenExpired(String token) {
	        return extractAllClaims(token).getExpiration().before(new Date());
	    }
	    
	    public static Long extractUserId(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)   // use the same key as in generateToken()
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        // JWT stores everything as Object, so convert properly
	        Object userIdObj = claims.get("userId");
	        if (userIdObj == null) {
	            return null;
	        }

	        if (userIdObj instanceof Integer) {
	            return ((Integer) userIdObj).longValue();
	        } else if (userIdObj instanceof Long) {
	            return (Long) userIdObj;
	        } else if (userIdObj instanceof String) {
	            return Long.parseLong((String) userIdObj);
	        } else {
	            throw new IllegalArgumentException("Unsupported userId type in JWT: " + userIdObj.getClass());
	        }
	    }
	    
}