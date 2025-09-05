package com.nikhil.inventory.adaptiveinventoryforecastingservice.logging;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.LogEntry;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.MainUserPrincipal;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.LogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private LogRepository logRepository;

    // Called before controller method execution
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = UUID.randomUUID().toString();
        request.setAttribute("correlationId", correlationId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.getPrincipal() instanceof MainUserPrincipal) {
            userId = ((MainUserPrincipal) auth.getPrincipal()).getid(); // numeric ID
        }
      
        LogEntry log = new LogEntry();
        log.setTimestamp(LocalDateTime.now());
        log.setUserId(userId); // If user is logged in, extract userId from SecurityContext
        log.setEventType("API_REQUEST");
        log.setEndpoint(request.getMethod() + " " + request.getRequestURI());
        log.setDetails("Params: " + request.getQueryString());
        log.setCorrelationId(correlationId);

     logRepository.save(log);
     
        return true; // continue with execution
    }

   

    // Called after request is fully completed (good place to log response status)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        String correlationId = (String) request.getAttribute("correlationId");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.getPrincipal() instanceof MainUserPrincipal) {
            userId = ((MainUserPrincipal) auth.getPrincipal()).getid(); // numeric ID
        }
        
        LogEntry log = new LogEntry();
        log.setTimestamp(LocalDateTime.now());
        log.setUserId(userId); // extract from SecurityContext if needed
        log.setEventType("API_RESPONSE");
        log.setEndpoint(request.getMethod() + " " + request.getRequestURI());
        log.setDetails("Status: " + response.getStatus() + 
                      (ex != null ? ", Exception: " + ex.getMessage() : ""));
        log.setCorrelationId(correlationId);

       logRepository.save(log);
    }
}

