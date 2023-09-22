package me.urninax.spotifystats.spotifyauth.repositories;

import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyCredentialsRepository extends JpaRepository<SpotifyCredentials, Long>{
    Optional<SpotifyCredentials> findByUser(User user);
}
