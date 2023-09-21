package me.urninax.spotifystats.spotifyauth.repositories;

import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyCredentialsRepository extends JpaRepository<SpotifyCredentials, Long>{
}
