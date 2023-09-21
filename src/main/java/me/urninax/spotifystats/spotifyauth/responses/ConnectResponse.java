package me.urninax.spotifystats.spotifyauth.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
Response with Spotify authorization link
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConnectResponse{
    private String link;
}
