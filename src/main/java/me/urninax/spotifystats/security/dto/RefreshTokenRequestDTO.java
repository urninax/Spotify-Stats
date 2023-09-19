package me.urninax.spotifystats.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequestDTO{
    @JsonProperty(value = "refresh_token")
    @NotEmpty(message = "Refresh token should not be blank")
    private String refreshToken;
}
