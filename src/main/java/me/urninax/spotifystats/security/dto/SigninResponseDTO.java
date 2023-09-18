package me.urninax.spotifystats.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninResponseDTO{
    public String username;
    public String refreshToken;
    public String accessToken;
    public String tokenType;
}
