package me.urninax.spotifystats.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.additional.ExternalUrlsDTO;
import me.urninax.spotifystats.components.dto.additional.SimplifiedArtistObjectDTO;

import java.util.List;

@Getter
@Setter
public class SpotifyTrackDTO{
    @JsonProperty("album")
    private SpotifyAlbumDTO album;

    @JsonProperty("artists")
    private List<SimplifiedArtistObjectDTO> artists;

    @JsonProperty("duration_ms")
    private int durationMs;

    @JsonProperty("external_urls")
    private ExternalUrlsDTO externalUrls;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private int popularity;
}
