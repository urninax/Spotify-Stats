package me.urninax.spotifystats.security.controllers;

import me.urninax.spotifystats.components.utils.GlobalResponse;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenExpiredException;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class RefreshTokenControllerAdvice{

    @ExceptionHandler({ RefreshTokenExpiredException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GlobalResponse expiredHandler(RefreshTokenExpiredException exc, WebRequest request){
        return new GlobalResponse(
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        );
    }

    @ExceptionHandler({ RefreshTokenNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalResponse notFoundHandler(RefreshTokenNotFoundException exc, WebRequest request){
        return new GlobalResponse(
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        );
    }
}
