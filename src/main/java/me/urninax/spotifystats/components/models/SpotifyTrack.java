package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "spotify_track")
@NoArgsConstructor
@Data public class SpotifyTrack{

    public SpotifyTrack(String spotifyId){
        this.spotifyId = spotifyId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String name;

    @Column(name = "popularity")
    private int popularity;

    @Column(name = "duration_ms")
    private int durationMs;

    @Column(name = "external_url")
    private String externalUrl;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private SpotifyAlbum album;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "spotify_track_artist",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<SpotifyArtist> artists = new LinkedList<>();

    @OneToMany(mappedBy = "track", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SpotifyFileStream> fileStreams = new LinkedList<>();

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SpotifyTrack that = (SpotifyTrack) o;
        return Objects.equals(spotifyId, that.spotifyId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(spotifyId);
    }
}
