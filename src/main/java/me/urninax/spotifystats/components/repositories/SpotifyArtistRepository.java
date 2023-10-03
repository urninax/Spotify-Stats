package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyArtistRepository extends JpaRepository<SpotifyArtist, Long>{
    Optional<SpotifyArtist> findSpotifyArtistBySpotifyId(String spotifyId);
}
