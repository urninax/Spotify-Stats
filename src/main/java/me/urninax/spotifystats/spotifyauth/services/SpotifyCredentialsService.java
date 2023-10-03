package me.urninax.spotifystats.spotifyauth.services;

import me.urninax.spotifystats.components.models.SpotifyUser;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
import me.urninax.spotifystats.spotifyauth.dto.RefreshedAccessTokenDTO;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import me.urninax.spotifystats.spotifyauth.repositories.SpotifyCredentialsRepository;
import me.urninax.spotifystats.spotifyauth.utils.SpotifyCredentialsHolder;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class SpotifyCredentialsService{
    private final SpotifyCredentialsRepository spotifyCredentialsRepository;
    private final SpotifyCredentialsHolder spotifyCredentialsHolder;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Autowired
    public SpotifyCredentialsService(SpotifyCredentialsRepository spotifyCredentialsRepository, SpotifyCredentialsHolder spotifyCredentialsHolder){
        this.spotifyCredentialsRepository = spotifyCredentialsRepository;
        this.spotifyCredentialsHolder = spotifyCredentialsHolder;
    }

    @Transactional
    public void save(SpotifyCredentials spotifyCredentials){
        spotifyCredentialsRepository.save(spotifyCredentials);
    }

    public String getAccessToken() throws SpotifyNotConnectedException, SpotifyServerErrorException{
        setSpotifyCredentials();
        Instant expiresAt = spotifyCredentialsHolder.getSpotifyCredentials().getExpiresAt();

        if(Instant.now().isAfter(expiresAt)){
            update(spotifyCredentialsHolder.getSpotifyCredentials());
        }

        return spotifyCredentialsHolder.getSpotifyCredentials().getAccessToken();
    }

    public SpotifyUser getLocalSpotifyUser() throws SpotifyNotConnectedException{
        setSpotifyCredentials();
        return spotifyCredentialsHolder.getSpotifyCredentials().getSpotifyUser();
    }

    public void setSpotifyCredentials() throws SpotifyNotConnectedException{
        if(spotifyCredentialsHolder.getSpotifyCredentials() == null){
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SpotifyCredentials credentials = userDetails.getUser().getSpotifyCredentials();

            // Optional<SpotifyCredentials> credentialsOptional = spotifyCredentialsRepository.findByUser(userDetails.getUser()); //use if User from ContextHolder doesn't have SpotifyCredentials
            if(credentials != null){
                spotifyCredentialsHolder.setSpotifyCredentials(credentials);
            }else{
                throw new SpotifyNotConnectedException("Spotify is not connected");
            }
        }
    }

    @Transactional
    public void update(SpotifyCredentials spotifyCredentials) throws SpotifyServerErrorException{
        RefreshedAccessTokenDTO dto = getResponse(spotifyCredentialsHolder.getSpotifyCredentials().getRefreshToken());

        spotifyCredentials.setAccessToken(dto.getAccessToken());
        spotifyCredentials.setExpiresAt(Instant.now().plusSeconds(dto.getExpiresIn()));

        spotifyCredentialsHolder.setSpotifyCredentials(spotifyCredentials);

        spotifyCredentialsRepository.save(spotifyCredentials);
    }

    private RefreshedAccessTokenDTO getResponse(String refreshToken) throws SpotifyServerErrorException{
        String appCredentials = String.format("%s:%s", clientId, clientSecret);
        String encodedAppCredentials = Base64.getUrlEncoder().encodeToString(appCredentials.getBytes());

        String tokenLink = "https://accounts.spotify.com/api/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", String.format("Basic %s", encodedAppCredentials));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<RefreshedAccessTokenDTO> response = restTemplate.exchange(
                tokenLink, HttpMethod.POST, httpEntity, RefreshedAccessTokenDTO.class);

        if(response.getBody() == null){
            throw new SpotifyServerErrorException("Spotify internal error");
        }
        return response.getBody();
    }
}
