package me.urninax.spotifystats.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.additional.ExternalUrlsDTO;
import me.urninax.spotifystats.components.dto.additional.FollowersDTO;

import java.util.List;

@Getter
@Setter
public class SpotifyArtistDTO{
    @JsonProperty("external_urls")
    private ExternalUrlsDTO externalUrls;

    @JsonProperty("followers")
    private FollowersDTO followers;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private List<SpotifyImageDTO> images;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private int popularity;
}
