package me.urninax.spotifystats.references.internal.uploading.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadErrorResponse{
    private Instant timestamp;
    private String message;
    private String path;
}
