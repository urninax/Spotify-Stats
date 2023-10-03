package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyAlbumRepository extends JpaRepository<SpotifyAlbum, Long>{
    Optional<SpotifyAlbum> findSpotifyAlbumBySpotifyId(String spotifyId);
}
