package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "spotify_user")
@NoArgsConstructor
@Data public class SpotifyUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "followers_number")
    private int followersNumber;

    @Column(name = "href")
    private String href;

    @Column(name = "last_fetched")
    private Instant lastFetched;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SpotifyImage image;

    @OneToOne
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private SpotifyCredentials credentials;

    @OneToMany(mappedBy = "spotifyUser")
    private List<SpotifyFileStream> fileStreams = new LinkedList<>();

    @OneToMany(mappedBy = "spotifyUser")
    private List<SpotifyUploadedFile> fileStats = new LinkedList<>();
}

