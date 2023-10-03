package me.urninax.spotifystats.components.services;

import me.urninax.spotifystats.components.models.SpotifyUser;
import me.urninax.spotifystats.components.models.SpotifyFileStream;
import me.urninax.spotifystats.components.repositories.SpotifyFileStreamRepository;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SpotifyFileStreamService{
    private final SpotifyFileStreamRepository fileStreamRepository;
    private final SpotifyCredentialsService spotifyCredentialsService;

    @Autowired
    public SpotifyFileStreamService(SpotifyFileStreamRepository fileStreamRepository, SpotifyCredentialsService spotifyCredentialsService){
        this.fileStreamRepository = fileStreamRepository;
        this.spotifyCredentialsService = spotifyCredentialsService;
    }

    @Transactional
    public void batchSave(List<SpotifyFileStream> streams) throws SpotifyNotConnectedException{
        SpotifyUser spotifyUser = spotifyCredentialsService.getLocalSpotifyUser();
        for(SpotifyFileStream stream : streams){
            stream.setSpotifyUser(spotifyUser);
            fileStreamRepository.save(stream);
        }
    }
}
