package com.nikhil.inventory.adaptiveinventoryforecastingservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions.UserAuthenticationException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.logging.BizLogs;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.MainUserPrincipal;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.AuthService;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.InMemoryCacheService;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.JwtUtilService;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	
	@Autowired
	private InMemoryCacheService cacheService;
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String ip = firstNonNull(request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        	
            if (cacheService.isTokenBlacklisted(token)) {
            	 BizLogs.AUTH.info("AUTH_ATTEMPT username={} ip={} result=SKIPPED reason=Token has been invalidated (logged out)", username, ip);
        	    throw new UserAuthenticationException("Token has been invalidated (logged out)");
        	}
             
             try {
				username = JwtUtilService.extractUsername(token);
			} catch (Exception e) {
				 BizLogs.AUTH.info("AUTH_ATTEMPT username={} ip={} result=SKIPPED reason="+e.getMessage(), username, ip);
				 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				    response.setContentType("application/json");
				    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
				    return;
			}
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
        	List<String> roles = JwtUtilService.extractRoles(token);
        	Long userId = JwtUtilService.extractUserId(token);
        	Collection<GrantedAuthority> authorities = roles.stream()
        	        .map(SimpleGrantedAuthority::new)
        	        .collect(Collectors.toList()); 
        	
        	MainUserPrincipal customUser =
        	        new MainUserPrincipal(userId, username, roles);
        	
        
        	
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(customUser, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            BizLogs.AUTH.info("AUTH_ATTEMPT username={} ip={} result=SUCCESS", username, ip);
        }
        
        chain.doFilter(request, response);
    }
    
    private static String firstNonNull(String a, String b){ return (a!=null && !a.isBlank()) ? a : b; }
}