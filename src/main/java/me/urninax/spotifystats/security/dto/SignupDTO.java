package me.urninax.spotifystats.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.ImmutableEntityEntry;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO{
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 40, message = "Username length should be between 2 and 40 characters")
    private String username;

    @Email
    @NotEmpty(message = "User email cannot be empty")
    private String email;

    private String password;
}
