package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.models.SpotifyArtist;
import me.urninax.spotifystats.components.repositories.SpotifyArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SpotifyArtistService{
    private final SpotifyArtistRepository spotifyArtistRepository;

    @Autowired
    public SpotifyArtistService(SpotifyArtistRepository spotifyArtistRepository){
        this.spotifyArtistRepository = spotifyArtistRepository;
    }

    public Optional<SpotifyArtist> findBySpotifyId(String spotifyId){
        return spotifyArtistRepository.findSpotifyArtistBySpotifyId(spotifyId);
    }
}
