package me.urninax.spotifystats.spotifyauth.controllers;

import jakarta.servlet.http.HttpServletResponse;
import me.urninax.spotifystats.components.dto.SpotifyImageDTO;
import me.urninax.spotifystats.components.dto.SpotifyUserDTO;
import me.urninax.spotifystats.components.models.SpotifyUser;
import me.urninax.spotifystats.components.utils.CustomObjectMapper;
import me.urninax.spotifystats.components.utils.GlobalResponse;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.services.UserService;
import me.urninax.spotifystats.spotifyauth.dto.SpotifyCredentialsDTO;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.AuthVerifier;
import me.urninax.spotifystats.spotifyauth.utils.providers.GlobalResponseProvider;
import me.urninax.spotifystats.spotifyauth.utils.providers.UsernameProvider;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/callback")
public class CallbackController{
    private final CustomObjectMapper customMapper;
    private final AuthVerifier authVerifier;
    private final GlobalResponseProvider globalResponseProvider;
    private final UserService userService;
    private final SpotifyCredentialsService spotifyCredentialsService;
    private final UsernameProvider usernameProvider; // should use UsernameProvider because Spotify callback doesn't save JWT Token in headers

    @Value("${redirectUri}")
    private String redirectUri;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public CallbackController(CustomObjectMapper customMapper, AuthVerifier authVerifier, GlobalResponseProvider globalResponseProvider, UserService userService, SpotifyCredentialsService spotifyCredentialsService, UsernameProvider usernameProvider){
        this.customMapper = customMapper;
        this.authVerifier = authVerifier;
        this.globalResponseProvider = globalResponseProvider;
        this.userService = userService;
        this.spotifyCredentialsService = spotifyCredentialsService;
        this.usernameProvider = usernameProvider;
    }

    @GetMapping()
    public void getCallback(@RequestParam(value = "code", required = false) String code,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "state") String state,
                              HttpServletResponse httpResponse) throws VerificationException, SpotifyServerErrorException{

        authVerifier.verify(state, code, error); //verify all the fields from Spotify server

        String spotifyRequestAccessTokenLink = "https://accounts.spotify.com/api/token";

        ResponseEntity<SpotifyCredentialsDTO> response = restTemplate.exchange(spotifyRequestAccessTokenLink,
                                                                                HttpMethod.POST,
                                                                                generateAccessTokenRequestHttpEntity(code),
                                                                                SpotifyCredentialsDTO.class);

        if(response.getBody() != null){
            spotifyCredentialsService.save(mapSpotifyCredentials(response.getBody(), Instant.now()));
        }else{
            throw new SpotifyServerErrorException("Didn't receive a response from Spotify server.");
        }

        try{
            httpResponse.sendRedirect("/api/callback/successful");  // redirects to user-friendly link
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/successful") // user-friendly authorization response
    public ResponseEntity<?> callbackSuccessful(WebRequest request){
        GlobalResponse response = new GlobalResponse(
                Instant.now(),
                "Verification successful",
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/failed") // user-friendly authorization response
    public ResponseEntity<?> callbackFailed(){
        return new ResponseEntity<>(globalResponseProvider.getGlobalResponse(), HttpStatus.FORBIDDEN);
    }

    public HttpEntity<?> generateAccessTokenRequestHttpEntity(String code){
        String appCredentials = String.format("%s:%s", clientId, clientSecret);
        String encodedAppCredentials = Base64.getUrlEncoder().encodeToString(appCredentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", String.format("Basic %s", encodedAppCredentials));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);

        return new HttpEntity<>(map, headers);
    }

    public SpotifyCredentials mapSpotifyCredentials(SpotifyCredentialsDTO dto, Instant getResponseAt) throws SpotifyServerErrorException{
        Optional<User> optionalUser = userService.findByUsername(usernameProvider.getUsername());

        SpotifyCredentials spotifyCredentials = new SpotifyCredentials();

        spotifyCredentials.setAccessToken(dto.getAccessToken());
        spotifyCredentials.setScope(dto.getScope());
        spotifyCredentials.setExpiresAt(getResponseAt.plusSeconds(dto.getExpiresIn()));
        spotifyCredentials.setRefreshToken(dto.getRefreshToken());

        optionalUser.ifPresent(spotifyCredentials::setUser);
        SpotifyUser spotifyUser = getSpotifyUser(dto.getAccessToken());
        spotifyCredentials.setSpotifyUser(spotifyUser);
        spotifyUser.setCredentials(spotifyCredentials);

        return spotifyCredentials;
    }

    public SpotifyUser getSpotifyUser(String accessToken) throws SpotifyServerErrorException{
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", accessToken));

        String link = "https://api.spotify.com/v1/me";

        ResponseEntity<SpotifyUserDTO> response = restTemplate.exchange(link, HttpMethod.GET, new HttpEntity<>(headers), SpotifyUserDTO.class);

        if(response.getBody() != null){
            SpotifyUserDTO spotifyUserDTO = response.getBody();
            return convertToSpotifyUser(spotifyUserDTO);
        }else{
            throw new SpotifyServerErrorException("Spotify server error.");
        }
    }

    public SpotifyUser convertToSpotifyUser(SpotifyUserDTO dto){
        SpotifyUser spotifyUser = new SpotifyUser();
        spotifyUser.setSpotifyId(dto.getSpotifyId());
        spotifyUser.setDisplayName(dto.getDisplayName());
        spotifyUser.setExternalUrl(dto.getExternalUrls().getUrl());
        spotifyUser.setHref(dto.getHref());
        spotifyUser.setFollowersNumber(dto.getFollowers().getTotal());

        List<SpotifyImageDTO> spotifyImages = dto.getImages();
        spotifyUser.setImage(customMapper.spotifyImageDTOtoEntity(spotifyImages));


        return spotifyUser;
    }
}
