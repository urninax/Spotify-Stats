package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.models.SpotifyTrack;
import me.urninax.spotifystats.components.repositories.SpotifyTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SpotifyTrackService{
    private final SpotifyTrackRepository spotifyTrackRepository;

    @Autowired
    public SpotifyTrackService(SpotifyTrackRepository spotifyTrackRepository){
        this.spotifyTrackRepository = spotifyTrackRepository;
    }

    public Optional<SpotifyTrack> findBySpotifyId(String spotifyId){
        return spotifyTrackRepository.findSpotifyTrackBySpotifyId(spotifyId);
    }

    @Transactional
    public SpotifyTrack save(SpotifyTrack spotifyTrack){
        return spotifyTrackRepository.save(spotifyTrack);
    }
}
