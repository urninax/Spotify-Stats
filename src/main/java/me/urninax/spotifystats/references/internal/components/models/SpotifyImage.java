package me.urninax.spotifystats.references.internal.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "spotify_image")
@NoArgsConstructor
@Data public class SpotifyImage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "height")
    private int height;

    @Column(name = "width")
    private int width;

    @OneToMany(mappedBy = "image")
    private List<SpotifyAlbum> albums;

    @OneToMany(mappedBy = "image")
    private List<SpotifyArtist> artists;

    @OneToMany(mappedBy = "image")
    private List<SpotifyTrack> tracks;
}
