package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyImageRepository extends JpaRepository<SpotifyImage, Long>{
    Optional<SpotifyImage> findSpotifyImageByHash(String hash);
}
