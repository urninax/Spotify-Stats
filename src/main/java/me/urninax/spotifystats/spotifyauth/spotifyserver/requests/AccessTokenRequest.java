package me.urninax.spotifystats.spotifyauth.spotifyserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccessTokenRequest{
    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";

    private String code;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    public AccessTokenRequest(String code, String redirectUri){
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
