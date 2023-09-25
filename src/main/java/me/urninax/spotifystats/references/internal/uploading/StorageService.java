package me.urninax.spotifystats.references.internal.uploading;

import me.urninax.spotifystats.security.models.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService{
    void init();

    void store(MultipartFile file, User user);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
