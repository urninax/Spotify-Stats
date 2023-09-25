package me.urninax.spotifystats.security.utils.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserErrorResponse{
    private Instant timestamp;
    private String message;
    private String path;
}
