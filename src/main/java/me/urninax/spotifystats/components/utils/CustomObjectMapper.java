package me.urninax.spotifystats.components.utils;

import me.urninax.spotifystats.components.dto.SpotifyImageDTO;
import me.urninax.spotifystats.components.dto.SpotifyTrackDTO;
import me.urninax.spotifystats.components.dto.additional.SeveralArtistsDTO;
import me.urninax.spotifystats.components.dto.additional.SimplifiedArtistObjectDTO;
import me.urninax.spotifystats.components.models.*;
import me.urninax.spotifystats.components.services.SpotifyAlbumService;
import me.urninax.spotifystats.components.services.SpotifyArtistService;
import me.urninax.spotifystats.components.services.SpotifyImageService;
import me.urninax.spotifystats.components.dto.SpotifyAlbumDTO;
import me.urninax.spotifystats.components.dto.SpotifyArtistDTO;
import me.urninax.spotifystats.components.dto.SpotifyFileStreamDTO;
import me.urninax.spotifystats.components.services.SpotifyTrackService;
import me.urninax.spotifystats.spotifyauth.services.SpotifyCredentialsService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomObjectMapper{
    private final SpotifyCredentialsService spotifyCredentialsService;
    private final SpotifyArtistService spotifyArtistService;
    private final SpotifyAlbumService spotifyAlbumService;
    private final SpotifyTrackService spotifyTrackService;
    private final SpotifyImageService spotifyImageService;
    private final SpotifyAPIRequests requests;

    @Autowired
    public CustomObjectMapper(SpotifyCredentialsService spotifyCredentialsService, SpotifyArtistService spotifyArtistService, SpotifyAlbumService spotifyAlbumService, SpotifyTrackService spotifyTrackService, SpotifyImageService spotifyImageService, SpotifyAPIRequests requests){
        this.spotifyCredentialsService = spotifyCredentialsService;
        this.spotifyArtistService = spotifyArtistService;
        this.spotifyAlbumService = spotifyAlbumService;
        this.spotifyTrackService = spotifyTrackService;
        this.spotifyImageService = spotifyImageService;
        this.requests = requests;
    }

    public SpotifyFileStream spotifyFileStreamDTOtoEntity(SpotifyFileStreamDTO dto) throws SpotifyNotConnectedException{
        SpotifyFileStream spotifyFileStream = new SpotifyFileStream();

        spotifyFileStream.setUsername(dto.getUsername());
        spotifyFileStream.setMsPlayed(dto.getMsPlayed());
        spotifyFileStream.setTrackName(dto.getTrackName());

        String[] spotifyUriParts = dto.getSpotifyTrackUri().split(":");

        spotifyFileStream.setSpotifyId(spotifyUriParts[spotifyUriParts.length-1]);
        spotifyFileStream.setPlayedAt(Instant.parse(dto.getTimestamp()));
        spotifyFileStream.setSpotifyUser(spotifyCredentialsService.getLocalSpotifyUser());

        return spotifyFileStream;
    }

    public SpotifyTrack spotifyTrackDTOtoEntity(SpotifyTrackDTO dto) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        Optional<SpotifyTrack> spotifyTrackOptional = spotifyTrackService.findBySpotifyId(dto.getId());

        if(spotifyTrackOptional.isPresent()){
            return spotifyTrackOptional.get();
        }

        SpotifyTrack spotifyTrack = new SpotifyTrack();

        spotifyTrack.setSpotifyId(dto.getId());
        spotifyTrack.setName(dto.getName());
        spotifyTrack.setPopularity(dto.getPopularity());
        spotifyTrack.setDurationMs(dto.getDurationMs());
        spotifyTrack.setExternalUrl(dto.getExternalUrls().getUrl());
        spotifyTrack.setImage(spotifyImageDTOtoEntity(dto.getAlbum().getImages()));
        spotifyTrack.setAlbum(spotifyAlbumDTOtoEntity(dto.getAlbum()));
        spotifyTrack.getArtists().addAll(resolveSimplifiedArtists(dto.getArtists()));

        //spotifyTrack.getArtists().addAll(dto.getArtists().stream().map(artist ->
                //spotifyArtistDTOtoEntity(artist, true)).toList());

//        spotifyTrack.setArtists(dto.getArtists().stream().map(artist ->
//                spotifyArtistDTOtoEntity(artist, true)).collect(Collectors.toList()));

        return spotifyTrackService.save(spotifyTrack);
    }

    public SpotifyAlbum spotifyAlbumDTOtoEntity(SpotifyAlbumDTO dto) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        Optional<SpotifyAlbum> spotifyAlbumOptional = spotifyAlbumService.findBySpotifyId(dto.getId());

        if(spotifyAlbumOptional.isPresent()){
            return spotifyAlbumOptional.get();
        }

        SpotifyAlbum spotifyAlbum = new SpotifyAlbum();

        spotifyAlbum.setSpotifyId(dto.getId());
        spotifyAlbum.setName(dto.getName());
        spotifyAlbum.setAlbumType(dto.getAlbumType());
        spotifyAlbum.setTotalTracks(dto.getTotalTracks());
        spotifyAlbum.setExternalUrl(dto.getExternalUrls().getUrl());
        spotifyAlbum.setReleaseDate(dto.getReleaseDate());
        spotifyAlbum.setReleaseDatePrecision(dto.getReleaseDatePrecision());
        spotifyAlbum.setImage(spotifyImageDTOtoEntity(dto.getImages()));
        spotifyAlbum.getArtists().addAll(resolveSimplifiedArtists(dto.getSimplifiedArtists())); //or get and addALl whole List if it doesn't work

        return spotifyAlbumService.save(spotifyAlbum);
    }

    public SpotifyArtist spotifyArtistDTOtoEntity(SpotifyArtistDTO dto, boolean check){
        if(check){ //is check in database required
            Optional<SpotifyArtist> spotifyArtistOptional = spotifyArtistService.findBySpotifyId(dto.getId());

            if(spotifyArtistOptional.isPresent()){
                return spotifyArtistOptional.get();
            }
        }

        SpotifyArtist spotifyArtist = new SpotifyArtist();

        spotifyArtist.setSpotifyId(dto.getId());
        spotifyArtist.setName(dto.getName());
        spotifyArtist.setExternalUrl(dto.getExternalUrls().getUrl());
        spotifyArtist.setFollowersNumber(dto.getFollowers().getTotal());
        spotifyArtist.setPopularity(dto.getPopularity());
        spotifyArtist.setImage(spotifyImageDTOtoEntity(dto.getImages()));

        return spotifyArtistService.save(spotifyArtist);
    }

    public SpotifyImage spotifyImageDTOtoEntity(List<SpotifyImageDTO> dtoList){
        if(!dtoList.isEmpty()){
            SpotifyImageDTO dto = dtoList.get(0);

            String[] urlParts = dto.getUrl().split("/");
            String hash = urlParts[urlParts.length-1];

            Optional<SpotifyImage> spotifyImageOptional = spotifyImageService.findByHash(hash);

            if(spotifyImageOptional.isPresent()){
                return spotifyImageOptional.get();
            }

            SpotifyImage spotifyImage = new SpotifyImage();

            spotifyImage.setUrl(dto.getUrl());
            spotifyImage.setHash(hash);
            spotifyImage.setWidth(dto.getWidth());
            spotifyImage.setHeight(dto.getHeight());

            return spotifyImageService.save(spotifyImage);
        }
        return null;
    }

    private List<SpotifyArtist> resolveSimplifiedArtists(List<SimplifiedArtistObjectDTO> simplifiedArtists) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        List<SpotifyArtist> artists = new LinkedList<>(); //container for retrieved artists

        StringBuilder unknownArtistsBuilder = new StringBuilder();

        for(int i = 0; i<simplifiedArtists.size(); i++){
            SimplifiedArtistObjectDTO dto = simplifiedArtists.get(i);
            Optional<SpotifyArtist> artistOptional = spotifyArtistService.findBySpotifyId(dto.getId()); //artist from db

            if(artistOptional.isPresent()){
                artists.add(artistOptional.get()); //add to all if exists
            }else{
                unknownArtistsBuilder.append(dto.getId());

                if((i % 50 == 0) || (i==simplifiedArtists.size()-1)){ // if there are 50 ids in StringBuilder or end of the list -> make a request to Spotify API
                    ResponseEntity<SeveralArtistsDTO> response = requests.getSeveralArtists(unknownArtistsBuilder.toString());//get info about unknown artists
                    unknownArtistsBuilder = new StringBuilder();
                    SeveralArtistsDTO severalArtistsDTO = response.getBody();

                    if(severalArtistsDTO != null){
                        artists.addAll(severalArtistsDTOtoEntities(severalArtistsDTO)); //add to all artists
                    }
                }else{ //otherwise append comma in the end
                    unknownArtistsBuilder.append(",");
                }

            }
        }

        return spotifyArtistService.saveAll(artists);
    }

    public List<SpotifyArtist> severalArtistsDTOtoEntities(SeveralArtistsDTO severalArtistsDTO){
        List<SpotifyArtistDTO> spotifyArtistDTOs = severalArtistsDTO.getArtists();
        List<SpotifyArtist> spotifyArtists = new LinkedList<>();

        for(SpotifyArtistDTO spotifyArtistDTO : spotifyArtistDTOs){
            spotifyArtists.add(spotifyArtistDTOtoEntity(spotifyArtistDTO, false));
        }

        return spotifyArtists;
    }
}
