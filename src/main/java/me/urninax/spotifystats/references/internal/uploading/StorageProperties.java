package me.urninax.spotifystats.references.internal.uploading;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties{
    private String location = "D:\\IdeaProjects\\Spotify Stats\\src\\main\\java\\me\\urninax\\spotifystats\\references\\internal\\uploading\\files";
}
