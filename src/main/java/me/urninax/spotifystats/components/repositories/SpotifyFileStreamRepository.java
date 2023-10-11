package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyFileStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SpotifyFileStreamRepository extends JpaRepository<SpotifyFileStream, Long>{
    Optional<SpotifyFileStream> findByUsernameAndPlayedAtAndSpotifyId(String username, Instant playedAt, String spotifyId);
}
