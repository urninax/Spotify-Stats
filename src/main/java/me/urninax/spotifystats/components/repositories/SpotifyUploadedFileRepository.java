package me.urninax.spotifystats.components.repositories;

import me.urninax.spotifystats.components.models.SpotifyUploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyUploadedFileRepository extends JpaRepository<SpotifyUploadedFile, Integer>{
}
