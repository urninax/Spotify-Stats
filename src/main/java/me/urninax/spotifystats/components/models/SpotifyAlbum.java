package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "spotify_album")
@NoArgsConstructor
@Data public class SpotifyAlbum{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String name;

    @Column(name = "album_type")
    private String albumType;

    @Column(name = "total_tracks")
    private int totalTracks;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "release_date_precision")
    private String releaseDatePrecision;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @ManyToMany
    @JoinTable(
            name = "spotify_artist_album",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<SpotifyArtist> artists = new LinkedList<>();

    @OneToMany(mappedBy = "album")
    private List<SpotifyTrack> tracks = new LinkedList<>();
}
