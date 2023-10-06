package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.models.SpotifyArtist;
import me.urninax.spotifystats.components.repositories.SpotifyArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public SpotifyArtist save(SpotifyArtist spotifyArtist){
        return spotifyArtistRepository.save(spotifyArtist);
    }

    @Transactional
    public List<SpotifyArtist> saveAll(List<SpotifyArtist> artists){
        return spotifyArtistRepository.saveAll(artists);
    }
}
