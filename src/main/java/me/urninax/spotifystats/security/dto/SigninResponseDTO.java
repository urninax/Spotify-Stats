package me.urninax.spotifystats.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder( {"username", "refresh_token", "access_token", "message", "timestamp"} )
public class SigninResponseDTO{
    public String username;

    @JsonProperty(value = "refresh_token")
    public String refreshToken;

    @JsonProperty(value = "access_token")
    public String accessToken;

    public String message;

    public Instant timestamp;
}
