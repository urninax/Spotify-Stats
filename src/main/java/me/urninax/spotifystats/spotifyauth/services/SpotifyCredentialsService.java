package me.urninax.spotifystats.spotifyauth.services;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.repositories.UserRepository;
import me.urninax.spotifystats.spotifyauth.dto.RefreshedAccessTokenDTO;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import me.urninax.spotifystats.spotifyauth.repositories.SpotifyCredentialsRepository;
import me.urninax.spotifystats.spotifyauth.spotifyserver.requests.RefreshedAccessTokenRequest;
import me.urninax.spotifystats.spotifyauth.utils.SpotifyCredentialsHolder;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

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

    public void verify(User user) throws SpotifyNotConnectedException, SpotifyServerErrorException{
        if(spotifyCredentialsHolder.getSpotifyCredentials() == null){ //check whether user spotify credentials is empty in SpotifyCredentialsHolder
            Optional<SpotifyCredentials> credentialsOptional = spotifyCredentialsRepository.findByUser(user);

            if(credentialsOptional.isPresent()){
                SpotifyCredentials credentials = credentialsOptional.get();
                spotifyCredentialsHolder.setSpotifyCredentials(credentials);
            }else{
                throw new SpotifyNotConnectedException("Spotify is not connected");
            }
        }
        Instant expiresAt = spotifyCredentialsHolder.getSpotifyCredentials().getExpiresAt();

        if(Instant.now().isAfter(expiresAt)){
            update(spotifyCredentialsHolder.getSpotifyCredentials());
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

        RefreshedAccessTokenRequest request = new RefreshedAccessTokenRequest(refreshToken);

        HttpEntity<RefreshedAccessTokenRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<RefreshedAccessTokenDTO> response = restTemplate.exchange(
                tokenLink, HttpMethod.POST, httpEntity, RefreshedAccessTokenDTO.class);

        if(response.getBody() == null){
            throw new SpotifyServerErrorException("Spotify internal error");
        }
        return response.getBody();
    }
}
