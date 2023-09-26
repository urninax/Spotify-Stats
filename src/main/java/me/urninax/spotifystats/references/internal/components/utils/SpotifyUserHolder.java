package me.urninax.spotifystats.references.internal.components.utils;

import me.urninax.spotifystats.references.internal.components.models.SpotifyUser;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
import me.urninax.spotifystats.spotifyauth.models.SpotifyCredentials;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpotifyUserHolder{
    private SpotifyUser spotifyUser;

    public SpotifyUser getSpotifyUser(){
        if(spotifyUser == null){
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SpotifyCredentials credentials = userDetails.getUser().getSpotifyCredentials();
            if(credentials != null){
                spotifyUser = credentials.getSpotifyUser();
            }else{
                //SpotifyNotConnectedException
            }
        }

        return spotifyUser;
    }

}
