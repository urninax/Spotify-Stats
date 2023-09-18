package me.urninax.spotifystats.security.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenExpiredException extends Exception{
    public RefreshTokenExpiredException(String token, String message){
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
