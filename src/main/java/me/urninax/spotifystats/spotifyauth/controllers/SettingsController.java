package me.urninax.spotifystats.spotifyauth.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.spotifyauth.utils.AuthLinkGeneration;
import me.urninax.spotifystats.spotifyauth.responses.ConnectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController{
    private AuthLinkGeneration linkGeneration;

    @PostMapping("/connect")
    public ResponseEntity<ConnectResponse> connectSpotifyAccount(){
        ConnectResponse connectResponse = new ConnectResponse(linkGeneration.generate());

        return new ResponseEntity<>(connectResponse, HttpStatus.OK);
    }
}
