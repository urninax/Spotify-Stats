package me.urninax.spotifystats.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.additional.ExternalUrlsDTO;
import me.urninax.spotifystats.components.dto.additional.FollowersDTO;

import java.util.List;

@Getter
@Setter
public class SpotifyUserDTO{
    @JsonProperty("id")
    private String spotifyId;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("external_urls")
    private ExternalUrlsDTO externalUrls;

    @JsonProperty("followers")
    private FollowersDTO followers;

    @JsonProperty
    private String href;

    @JsonProperty("images")
    private List<SpotifyImageDTO> images;
}
