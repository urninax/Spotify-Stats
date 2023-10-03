package me.urninax.spotifystats.components.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spotify_uploaded_file")
@NoArgsConstructor
@Data public class SpotifyUploadedFile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "streams_count")
    private int streamsCount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private SpotifyUser spotifyUser;
}
