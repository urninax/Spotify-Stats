package me.urninax.spotifystats.references.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.SpotifyCredentialsHolder;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@AllArgsConstructor
@RequestMapping("/ref/me")
public class UsersController{
    private final RestTemplate restTemplate = new RestTemplate();
    private SpotifyCredentialsService spotifyCredentialsService;
    private SpotifyCredentialsHolder spotifyCredentialsHolder;

    @GetMapping("/top/{type}")
    public void getUsersTopItems(@PathVariable("type") String type) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", getAccessToken()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        String link = "https://api.spotify.com/v1/me/top/%s";

        System.out.println(restTemplate.exchange(String.format(link, type), HttpMethod.GET, entity, String.class));
    }

    public String getAccessToken() throws SpotifyServerErrorException, SpotifyNotConnectedException{
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        spotifyCredentialsService.verify(userDetails.getUser());

        return spotifyCredentialsHolder.getSpotifyCredentials().getAccessToken();
    }
}
