package me.urninax.spotifystats.spotifyauth.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallbackResponse{
    private String message;
    private Instant timestamp;
}
