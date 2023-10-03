package me.urninax.spotifystats.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@Data public class SpotifyFileStreamDTO{
    private String username;

    @JsonProperty("ms_played")
    private int msPlayed;

    @JsonProperty("spotify_track_uri")
    private String spotifyTrackUri;

    @JsonProperty("ts")
    private String timestamp;

    @JsonProperty("master_metadata_track_name")
    private String trackName;
}
