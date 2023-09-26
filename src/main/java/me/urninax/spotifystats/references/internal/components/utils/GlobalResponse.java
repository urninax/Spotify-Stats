package me.urninax.spotifystats.references.internal.components.utils;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResponse{
    private Instant timestamp;
    private String message;
    private String path;
}
