package me.urninax.spotifystats.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class RefreshAccessTokenResponse{
    @JsonProperty(value = "access_token")
    private String accessToken;

    private String type = "Bearer";

    @JsonProperty(value = "issued_at")
    private Instant issuedAt;

    @JsonProperty(value = "expires_at")
    private Instant expiresAt;

    public RefreshAccessTokenResponse(String accessToken, Instant issuedAt, Instant expiresAt){
        this.accessToken = accessToken;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}
