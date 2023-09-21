package me.urninax.spotifystats.spotifyauth.utils;

import lombok.Getter;
import lombok.Setter;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.VerificationException;

@Getter
@Setter
public class AuthVerifier{
    public String state;

    public void verify(String responseState, String code, String error) throws VerificationException{
        if(!responseState.equals(state) || (code == null && error == null) || (code != null && error != null)){ //check whether states are identical, code & error don't appear simultaneously/at least one field appears
            throw new VerificationException("Verification failed.");
        }

        if(error != null){
            throw new VerificationException(String.format("Verification failed. Reason: %s", error)); //throw exception with reason from spotify server
        }
    }
}
