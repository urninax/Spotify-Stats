package me.urninax.spotifystats.references.internal.uploading.utils;

import java.util.Optional;

public class Extension{
    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
