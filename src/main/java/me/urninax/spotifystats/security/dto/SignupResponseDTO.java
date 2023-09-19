package me.urninax.spotifystats.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponseDTO{
    private String message;
    private Instant timestamp;
}
