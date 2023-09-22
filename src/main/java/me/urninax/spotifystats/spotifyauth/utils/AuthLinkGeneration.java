package me.urninax.spotifystats.spotifyauth.utils;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@PropertySource("classpath:application.properties")
@NoArgsConstructor
public class AuthLinkGeneration{
    private AuthVerifier authVerifier;
    private final static String url = "https://accounts.spotify.com/authorize?";
    private final static String responseType = "code";

    @Value("${clientId}")
    private String clientId;

    @Value("${redirectUri}")
    private String redirectUri;

    @Value("${scope}")
    private String scope;

    @Autowired
    public AuthLinkGeneration(AuthVerifier authVerifier){
        this.authVerifier = authVerifier;
    }

    private String generateRandomString(int length){
        StringBuilder stringBuilder = new StringBuilder();
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

        Random random = new Random();

        for(int i = 0; i<length; i++){
            stringBuilder.append(possible.charAt(random.nextInt(possible.length())));
        }

        return stringBuilder.toString();
    }

    public String generate(){
        String state = generateRandomString(16);

        authVerifier.setState(state); //set generated state to compare with received from spotify server

        Map<String, String> parameters = new HashMap<>();

        parameters.put("client_id", clientId);
        parameters.put("response_type", responseType);
        parameters.put("redirect_uri", redirectUri);
        parameters.put("state", state);
        parameters.put("scope", scope);

        return generateLink(parameters);
    }

    private static String generateLink(Map<String, String> parameters){ // build link with all query parameters
        StringBuilder stringBuilder = new StringBuilder(url);
        for(Map.Entry<?, ?> entry : parameters.entrySet()){
            stringBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        String link = stringBuilder.toString();
        return link + "show_dialog=true";
    }

}
