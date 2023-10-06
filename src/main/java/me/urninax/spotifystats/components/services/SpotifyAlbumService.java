package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.repositories.SpotifyAlbumRepository;
import me.urninax.spotifystats.components.models.SpotifyAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SpotifyAlbumService{
    private final SpotifyAlbumRepository spotifyAlbumRepository;

    @Autowired
    public SpotifyAlbumService(SpotifyAlbumRepository spotifyAlbumRepository){
        this.spotifyAlbumRepository = spotifyAlbumRepository;
    }

    public Optional<SpotifyAlbum> findBySpotifyId(String spotifyId){
        return spotifyAlbumRepository.findSpotifyAlbumBySpotifyId(spotifyId);
    }

    @Transactional
    public SpotifyAlbum save(SpotifyAlbum spotifyAlbum){
        return spotifyAlbumRepository.save(spotifyAlbum);
    }
}
