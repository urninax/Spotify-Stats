package me.urninax.spotifystats.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.urninax.spotifystats.security.enums.ERole;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 40, message = "Username length should be between 2 and 40 characters")
    @Column(name="username")
    private String username;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERole role;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private SpotifyCredentials spotifyCredentials;
}
