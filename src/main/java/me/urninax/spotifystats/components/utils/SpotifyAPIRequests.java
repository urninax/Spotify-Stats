package me.urninax.spotifystats.components.utils;

import me.urninax.spotifystats.components.dto.additional.SeveralArtistsDTO;
import me.urninax.spotifystats.components.dto.additional.SeveralTracksDTO;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SpotifyAPIRequests{
    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotifyCredentialsService spotifyCredentialsService;
    private long requestAt = System.currentTimeMillis();

    @Autowired
    public SpotifyAPIRequests(SpotifyCredentialsService spotifyCredentialsService){
        this.spotifyCredentialsService = spotifyCredentialsService;
    }

    public ResponseEntity<SeveralTracksDTO> getSeveralTracks(String ids) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        wait400ms();

        String url = "https://api.spotify.com/v1/tracks?ids=%s";
       // String encodedIds = URLEncoder.encode(ids, StandardCharsets.UTF_8);

        String requestLink = String.format(url, ids);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", spotifyCredentialsService.getAccessToken()));

        ResponseEntity<SeveralTracksDTO> response = restTemplate.exchange(requestLink, HttpMethod.GET, new HttpEntity<>(headers), SeveralTracksDTO.class);

        requestAt = System.currentTimeMillis();

        return response;
    }

    public ResponseEntity<SeveralArtistsDTO> getSeveralArtists(String ids) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        wait400ms();

        String url = "https://api.spotify.com/v1/artists?ids=%s";
        //String encodedIds = URLEncoder.encode(ids, StandardCharsets.UTF_8);

        String requestLink = String.format(url, ids);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", spotifyCredentialsService.getAccessToken()));

        ResponseEntity<SeveralArtistsDTO> response = restTemplate.exchange(requestLink, HttpMethod.GET, new HttpEntity<>(headers), SeveralArtistsDTO.class);

        requestAt = System.currentTimeMillis();

        return response;
    }

    public void wait400ms(){
        long now = System.currentTimeMillis();
        long requestAllowedAt = requestAt+200;

        if(requestAllowedAt>now){
            try{
                Thread.sleep(requestAllowedAt-now);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
