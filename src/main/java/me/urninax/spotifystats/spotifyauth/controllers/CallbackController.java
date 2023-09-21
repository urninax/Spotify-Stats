package me.urninax.spotifystats.spotifyauth.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import me.urninax.spotifystats.spotifyauth.requests.AccessTokenRequest;
import me.urninax.spotifystats.spotifyauth.responses.CallbackResponse;
import me.urninax.spotifystats.spotifyauth.utils.AuthVerifier;
import me.urninax.spotifystats.spotifyauth.utils.CallbackResponseProvider;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.VerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

@RestController
@RequestMapping("/api/callback")
public class CallbackController{
    private AuthVerifier authVerifier;
    private CallbackResponseProvider callbackResponseProvider;

    @Value("${redirectUri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public CallbackController(AuthVerifier authVerifier, CallbackResponseProvider callbackResponseProvider){
        this.authVerifier = authVerifier;
        this.callbackResponseProvider = callbackResponseProvider;
    }

    @GetMapping()
    public void getCallback(@RequestParam(value = "code", required = false) String code,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "state") String state,
                              HttpServletResponse httpResponse) throws VerificationException{

        authVerifier.verify(state, code, error); //verify all the fields from Spotify server

        AccessTokenRequest accessTokenRequest = new AccessTokenRequest(code, redirectUri);



        try{
            httpResponse.sendRedirect("/api/callback/successful");  // redirects to user-friendly link
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/successful") // user-friendly authorization response
    public ResponseEntity<?> callbackSuccessful(){
        CallbackResponse callbackResponse = new CallbackResponse(
                "Verification successful",
                Instant.now()
        );

        return new ResponseEntity<>(callbackResponse, HttpStatus.OK);
    }

    @GetMapping("/failed") // user-friendly authorization response
    public ResponseEntity<?> callbackFailed(){
        return new ResponseEntity<>(callbackResponseProvider.getCallbackResponse(), HttpStatus.FORBIDDEN);
    }
}
