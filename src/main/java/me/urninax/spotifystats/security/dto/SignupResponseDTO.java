package me.urninax.spotifystats.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO{
    private String message;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;
    private Instant timestamp;
}
