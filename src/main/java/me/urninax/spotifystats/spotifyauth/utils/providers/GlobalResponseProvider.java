package me.urninax.spotifystats.spotifyauth.utils.providers;

import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.utils.GlobalResponse;
import org.springframework.stereotype.Component;

/*
Provides callback response with reason between CallbackControllerAdvice and CallbackController
 */

@Getter
@Setter
@Component
public class GlobalResponseProvider{
    private GlobalResponse globalResponse;
}
