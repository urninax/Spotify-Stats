package me.urninax.spotifystats.security.utils.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserErrorResponse{
    private String errors;
    private LocalDateTime timestamp;
}
