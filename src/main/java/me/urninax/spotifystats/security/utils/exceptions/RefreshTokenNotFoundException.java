package me.urninax.spotifystats.security.utils.exceptions;

public class RefreshTokenNotFoundException extends Exception{
    public RefreshTokenNotFoundException(String message){
        super(message);
    }
}
