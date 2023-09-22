package me.urninax.spotifystats.security.utils.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTErrorResponse{
    private String message;
    private Instant timestamp;
    private String path;
}
