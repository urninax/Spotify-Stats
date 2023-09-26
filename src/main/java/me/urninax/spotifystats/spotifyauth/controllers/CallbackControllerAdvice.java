package me.urninax.spotifystats.spotifyauth.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import me.urninax.spotifystats.references.internal.components.utils.GlobalResponse;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.providers.GlobalResponseProvider;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.VerificationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
@AllArgsConstructor
public class CallbackControllerAdvice{
    private GlobalResponseProvider globalResponseProvider;


    @ExceptionHandler( {VerificationException.class} )
    public void verificationHandler(VerificationException exc,
                                    HttpServletResponse response, WebRequest request){

        globalResponseProvider.setGlobalResponse(new GlobalResponse(//set generated CallbackResponse to Provider
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        ));

        try{
            response.sendRedirect("/api/callback/failed"); //redirects to user-friendly link
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler({SpotifyServerErrorException.class, SpotifyNotConnectedException.class})
    public GlobalResponse spotifyServerHandler(Exception exc, WebRequest request){
        return new GlobalResponse(Instant.now(), exc.getMessage(), request.getDescription(false).substring(4));
    }
}
