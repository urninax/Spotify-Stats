package me.urninax.spotifystats.spotifyauth.services;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import me.urninax.spotifystats.spotifyauth.repositories.SpotifyCredentialsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SpotifyCredentialsService{
    private final SpotifyCredentialsRepository spotifyCredentialsRepository;

    @Transactional
    public void save(SpotifyCredentials spotifyCredentials){
        spotifyCredentialsRepository.save(spotifyCredentials);
    }
}
