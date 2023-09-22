package me.urninax.spotifystats.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.urninax.spotifystats.security.utils.responses.JWTErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.time.Instant;

@ControllerAdvice
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = String.format("%s. Check the validity of Access Token", authException.getMessage());

        JWTErrorResponse jwtErrorResponse = new JWTErrorResponse(message, Instant.now(), request.getServletPath());

        ObjectMapper mapper = configureObjectMapper();
        mapper.writeValue(response.getOutputStream(), jwtErrorResponse);
    }

    private ObjectMapper configureObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper;
    }
}
