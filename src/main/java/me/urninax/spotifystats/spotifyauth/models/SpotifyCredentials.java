package me.urninax.spotifystats.spotifyauth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.urninax.spotifystats.security.models.User;

import java.time.Instant;

@Entity
@Table(name = "spotify_credentials")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpotifyCredentials{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "scope")
    private String scope;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
