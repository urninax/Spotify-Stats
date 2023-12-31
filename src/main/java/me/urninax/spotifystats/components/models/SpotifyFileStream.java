package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "spotify_file_stream",
        indexes = {@Index(name = "file_stream_username_played_at_spotify_id_index", columnList = "username, played_at, spotify_id")})
@Data public class SpotifyFileStream{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "track_name")
    private String trackName;

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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SpotifyFileStream that = (SpotifyFileStream) o;
        return Objects.equals(username, that.username) && Objects.equals(spotifyId, that.spotifyId) && Objects.equals(playedAt, that.playedAt);
    }

    @Override
    public int hashCode(){
        return Objects.hash(username, spotifyId, playedAt);
    }
}
