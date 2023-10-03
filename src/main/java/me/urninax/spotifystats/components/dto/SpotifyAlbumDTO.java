package me.urninax.spotifystats.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.additional.ExternalUrlsDTO;
import me.urninax.spotifystats.components.dto.additional.SimplifiedArtistObjectDTO;

import java.util.List;


@Getter
@Setter
public class SpotifyAlbumDTO{
    @JsonProperty("album_type")
    private String albumType;

    @JsonProperty("total_tracks")
    private int totalTracks;

    @JsonProperty("external_urls")
    private ExternalUrlsDTO externalUrls;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private List<SpotifyImageDTO> images;

    @JsonProperty("name")
    private String name;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;

    @JsonProperty("artists")
    private List<SimplifiedArtistObjectDTO> simplifiedArtists;
}
