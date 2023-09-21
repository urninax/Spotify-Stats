package me.urninax.spotifystats.spotifyauth.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
import me.urninax.spotifystats.spotifyauth.utils.AuthLinkGeneration;
import me.urninax.spotifystats.spotifyauth.responses.ConnectResponse;
import me.urninax.spotifystats.spotifyauth.utils.UsernameProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController{
    private AuthLinkGeneration linkGeneration;
    private UsernameProvider usernameProvider;

    @PostMapping("/connect")
    public ResponseEntity<ConnectResponse> connectSpotifyAccount(){
        ConnectResponse connectResponse = new ConnectResponse(linkGeneration.generate());

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usernameProvider.setUsername(userDetails.getUsername());

        return new ResponseEntity<>(connectResponse, HttpStatus.OK);
    }
}
