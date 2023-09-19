package me.urninax.spotifystats.security.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.security.models.RefreshToken;

@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenRequestDTO{
    @NotBlank(message = "Refresh token should not be blank")
    private String refreshToken;
}
