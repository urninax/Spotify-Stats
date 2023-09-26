package me.urninax.spotifystats.references.internal.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "spotify_track")
@NoArgsConstructor
@Data public class SpotifyTrack{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private SpotifyAlbum album;

    @ManyToMany
    @JoinTable(
            name = "spotify_track_artist",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<SpotifyArtist> artists;
}
