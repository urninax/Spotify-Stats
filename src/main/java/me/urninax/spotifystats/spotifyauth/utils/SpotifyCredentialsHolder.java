package me.urninax.spotifystats.spotifyauth.utils;

import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SpotifyCredentialsHolder{
    private SpotifyCredentials spotifyCredentials;
}
