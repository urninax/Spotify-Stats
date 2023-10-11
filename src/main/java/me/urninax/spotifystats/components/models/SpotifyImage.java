package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "spotify_image",
    indexes = {@Index(columnList = "hash", name = "image_hash_index", unique = true)})
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

    @Column(name = "hash")
    private String hash;

    @OneToMany(mappedBy = "image")
    private List<SpotifyAlbum> albums = new LinkedList<>();

    @OneToMany(mappedBy = "image")
    private List<SpotifyArtist> artists = new LinkedList<>();

    @OneToMany(mappedBy = "image")
    private List<SpotifyTrack> tracks = new LinkedList<>();
}
