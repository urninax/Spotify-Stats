package me.urninax.spotifystats.components.dto.additional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.SpotifyTrackDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeveralTracksDTO{
    @JsonProperty("tracks")
    public List<SpotifyTrackDTO> tracks;
}
