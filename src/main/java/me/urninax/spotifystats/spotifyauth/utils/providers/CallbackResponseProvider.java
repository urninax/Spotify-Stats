package me.urninax.spotifystats.spotifyauth.utils.providers;

import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.spotifyauth.app.responses.CallbackResponse;
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
