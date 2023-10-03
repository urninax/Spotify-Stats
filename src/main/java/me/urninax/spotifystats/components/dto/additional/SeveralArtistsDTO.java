package me.urninax.spotifystats.components.dto.additional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.components.dto.SpotifyArtistDTO;

import java.util.List;

@Getter
@Setter
public class SeveralArtistsDTO{
    @JsonProperty("artists")
    private List<SpotifyArtistDTO> artists;
}
