package me.urninax.spotifystats.spotifyauth.spotifyserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshedAccessTokenRequest{
    private String grantType = "refresh_token";

    @JsonProperty("refresh_token")
    private String refreshToken;

    public RefreshedAccessTokenRequest(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
