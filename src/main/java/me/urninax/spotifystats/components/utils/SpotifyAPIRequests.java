package me.urninax.spotifystats.components.utils;

import me.urninax.spotifystats.components.dto.additional.SeveralArtistsDTO;
import me.urninax.spotifystats.components.dto.additional.SeveralTracksDTO;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class SpotifyAPIRequests{
    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotifyCredentialsService spotifyCredentialsService;

    @Autowired
    public SpotifyAPIRequests(SpotifyCredentialsService spotifyCredentialsService){
        this.spotifyCredentialsService = spotifyCredentialsService;
    }

    public ResponseEntity<SeveralTracksDTO> getSeveralTracks(String ids) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        String url = "https://api.spotify.com/v1/tracks?%s";
        String encodedIds = URLEncoder.encode(ids, StandardCharsets.UTF_8);

        String requestLink = String.format(url, encodedIds);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", spotifyCredentialsService.getAccessToken()));

        return restTemplate.exchange(requestLink, HttpMethod.GET, new HttpEntity<>(headers), SeveralTracksDTO.class);
    }

    public ResponseEntity<SeveralArtistsDTO> getSeveralArtists(String ids) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        String url = "https://api.spotify.com/v1/artists?%s";
        String encodedIds = URLEncoder.encode(ids, StandardCharsets.UTF_8);

        String requestLink = String.format(url, encodedIds);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", spotifyCredentialsService.getAccessToken()));

        return restTemplate.exchange(requestLink, HttpMethod.GET, new HttpEntity<>(headers), SeveralArtistsDTO.class);
    }
}
