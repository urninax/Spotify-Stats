package me.urninax.spotifystats.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninResponseDTO{
    public String username;
    public String refreshToken;
    public String accessToken;
    public String tokenType;
}
