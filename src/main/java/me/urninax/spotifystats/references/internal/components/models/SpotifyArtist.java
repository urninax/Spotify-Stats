package me.urninax.spotifystats.references.internal.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "spotify_artist")
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

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @ManyToMany(mappedBy = "artists")
    private List<SpotifyAlbum> albums;

    @ManyToMany(mappedBy = "artists")
    private List<SpotifyTrack> tracks;

}
