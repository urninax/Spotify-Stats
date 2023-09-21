package me.urninax.spotifystats.spotifyauth.utils;

import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.spotifyauth.responses.CallbackResponse;
import org.springframework.stereotype.Component;

/*
Provides callback response with reason between CallbackControllerAdvice and CallbackController
 */

@Getter
@Setter
@Component
public class CallbackResponseProvider{
    private CallbackResponse callbackResponse;
}
