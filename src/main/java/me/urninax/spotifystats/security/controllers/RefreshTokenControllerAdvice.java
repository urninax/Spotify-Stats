package me.urninax.spotifystats.security.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.services.RefreshTokenService;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenExpiredException;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenNotFoundException;
import me.urninax.spotifystats.security.utils.responses.RefreshTokenErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class RefreshTokenControllerAdvice{

    @ExceptionHandler({ RefreshTokenExpiredException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RefreshTokenErrorResponse expiredHandler(RefreshTokenExpiredException exc, WebRequest request){
        return new RefreshTokenErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                Instant.now(),
                exc.getMessage(),
                request.getContextPath()); // substring output if appears not so as intended
    }

    @ExceptionHandler({ RefreshTokenNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RefreshTokenErrorResponse notFoundHandler(RefreshTokenNotFoundException exc, WebRequest request){
        return new RefreshTokenErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                Instant.now(),
                exc.getMessage(),
                request.getContextPath() // substring output if appears not so as intended
        );
    }
}
