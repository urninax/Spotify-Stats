package me.urninax.spotifystats.references.internal.uploading;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties{
    @Value("${filesLocation}")
    private String filesLocation;
}
