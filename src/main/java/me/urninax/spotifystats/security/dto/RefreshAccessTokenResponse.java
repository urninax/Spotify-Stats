package me.urninax.spotifystats.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
public class RefreshAccessTokenResponse{
    private String accessToken;
    private Instant issuedAt;
    private Instant expiresAt;
}
