package me.urninax.spotifystats.references.internal.components.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "spotify_file_stream")
public class SpotifyFileStream{
    private Long id;
    private String username;
    private int msPlayed;
    private String spotifyId;
    private Instant playedAt;
    private SpotifyUser spotifyUser;
}
