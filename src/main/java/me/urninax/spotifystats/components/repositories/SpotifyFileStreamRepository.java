package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyFileStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyFileStreamRepository extends JpaRepository<SpotifyFileStream, Long>{
}
