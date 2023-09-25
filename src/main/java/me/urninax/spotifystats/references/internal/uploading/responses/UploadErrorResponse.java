package me.urninax.spotifystats.references.internal.uploading.responses;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class UploadErrorResponse{
    private Instant timestamp;
    private String message;
    private String path;
}
