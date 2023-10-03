package me.urninax.spotifystats.components.dto.additional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowersDTO{
    @JsonProperty("total")
    private int total;
}
