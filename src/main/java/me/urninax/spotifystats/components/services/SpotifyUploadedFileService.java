package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.models.SpotifyUploadedFile;
import me.urninax.spotifystats.components.repositories.SpotifyUploadedFileRepository;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SpotifyUploadedFileService{
    private final SpotifyUploadedFileRepository spotifyUploadedFileRepository;
    private final SpotifyCredentialsService spotifyCredentialsService;

    @Autowired
    public SpotifyUploadedFileService(SpotifyUploadedFileRepository spotifyUploadedFileRepository, SpotifyCredentialsService spotifyCredentialsService){
        this.spotifyUploadedFileRepository = spotifyUploadedFileRepository;
        this.spotifyCredentialsService = spotifyCredentialsService;
    }

    @Transactional
    public void save(String filename, int streamsCount) throws SpotifyNotConnectedException{
        SpotifyUploadedFile spotifyUploadedFile = new SpotifyUploadedFile();
        spotifyUploadedFile.setFilename(filename);
        spotifyUploadedFile.setStreamsCount(streamsCount);
        spotifyUploadedFile.setSpotifyUser(spotifyCredentialsService.getLocalSpotifyUser());

        spotifyUploadedFileRepository.save(spotifyUploadedFile);
    }
}
