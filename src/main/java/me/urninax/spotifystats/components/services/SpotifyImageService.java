package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.repositories.SpotifyImageRepository;
import me.urninax.spotifystats.components.models.SpotifyImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SpotifyImageService{
    private final SpotifyImageRepository spotifyImageRepository;

    @Autowired
    public SpotifyImageService(SpotifyImageRepository spotifyImageRepository){
        this.spotifyImageRepository = spotifyImageRepository;

    }

    public Optional<SpotifyImage> findByHash(String hash){
        return spotifyImageRepository.findSpotifyImageByHash(hash);
    }
}
