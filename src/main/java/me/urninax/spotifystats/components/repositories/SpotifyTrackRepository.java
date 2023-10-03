package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyTrackRepository extends JpaRepository<SpotifyTrack, Long>{
    Optional<SpotifyTrack> findSpotifyTrackBySpotifyId(String spotifyId);
}
