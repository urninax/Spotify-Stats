package me.urninax.spotifystats.components.dto.additional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedArtistObjectDTO{
    @JsonProperty("id")
    private String id;
}
