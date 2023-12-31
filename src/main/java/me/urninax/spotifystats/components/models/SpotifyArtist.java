package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "spotify_artist",
        indexes = {@Index(columnList = "spotify_id", name = "artist_spotify_id_index", unique = true)})
@NoArgsConstructor
@Data public class SpotifyArtist{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String name;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "followers_number")
    private int followersNumber;

    @Column(name = "popularity")
    private int popularity;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @ManyToMany(mappedBy = "artists")
    private List<SpotifyAlbum> albums = new LinkedList<>();

    @ManyToMany(mappedBy = "artists")
    private List<SpotifyTrack> tracks = new LinkedList<>();
}
