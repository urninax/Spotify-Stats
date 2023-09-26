package me.urninax.spotifystats.references.internal.components.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "spotify_file_stream")
@Data public class SpotifyFileStream{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "ms_played")
    private int msPlayed;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "played_at")
    private Instant playedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private SpotifyUser spotifyUser;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "id")
    private SpotifyTrack track;
}
