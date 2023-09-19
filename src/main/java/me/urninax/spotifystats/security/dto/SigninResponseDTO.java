package me.urninax.spotifystats.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninResponseDTO{
    public String username;
    public String refreshToken;
    public String accessToken;
    public String tokenType = "Bearer";

    public SigninResponseDTO(String username, String refreshToken, String accessToken){
        this.username = username;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
