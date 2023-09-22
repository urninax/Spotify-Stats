package me.urninax.spotifystats.spotifyauth.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import me.urninax.spotifystats.spotifyauth.app.responses.CallbackResponse;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.providers.CallbackResponseProvider;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.VerificationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
@AllArgsConstructor
public class CallbackControllerAdvice{
    private CallbackResponseProvider callbackResponseProvider;


    @ExceptionHandler( {VerificationException.class} )
    public void verificationHandler(VerificationException exc,
                                                HttpServletResponse response){

        callbackResponseProvider.setCallbackResponse(new CallbackResponse( //set generated CallbackResponse to Provider
                exc.getMessage(),
                Instant.now()
        ));

        try{
            response.sendRedirect("/api/callback/failed"); //redirects to user-friendly link
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(SpotifyServerErrorException.class)
    public CallbackResponse spotifyServerHandler(SpotifyServerErrorException exc){
        return new CallbackResponse(exc.getMessage(), Instant.now());
    }

    @ExceptionHandler(SpotifyNotConnectedException.class)
    public CallbackResponse spotifyNotConnectedHandler(SpotifyNotConnectedException exc){
        return new CallbackResponse(exc.getMessage(), Instant.now());
    }
}
