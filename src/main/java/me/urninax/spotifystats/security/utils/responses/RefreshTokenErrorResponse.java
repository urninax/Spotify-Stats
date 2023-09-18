package me.urninax.spotifystats.security.utils.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenErrorResponse{
    private int statusCode;
    private Instant timestamp;
    private String message;
    private String path;
}
