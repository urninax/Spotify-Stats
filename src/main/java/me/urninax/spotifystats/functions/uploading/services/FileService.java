package me.urninax.spotifystats.functions.uploading.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.urninax.spotifystats.components.dto.SpotifyTrackDTO;
import me.urninax.spotifystats.components.dto.additional.SeveralTracksDTO;
import me.urninax.spotifystats.components.models.SpotifyFileStream;
import me.urninax.spotifystats.components.models.SpotifyTrack;
import me.urninax.spotifystats.components.services.SpotifyTrackService;
import me.urninax.spotifystats.components.services.SpotifyUploadedFileService;
import me.urninax.spotifystats.components.utils.CustomObjectMapper;
import me.urninax.spotifystats.components.utils.SpotifyAPIRequests;
import me.urninax.spotifystats.components.dto.SpotifyFileStreamDTO;
import me.urninax.spotifystats.functions.uploading.utils.Extension;
import me.urninax.spotifystats.functions.uploading.exceptions.StorageException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileService{
    private final ObjectMapper mapper;
    private final CustomObjectMapper customMapper;
    private final SpotifyAPIRequests requests;
    private final SpotifyUploadedFileService uploadedFileService;
    private final SpotifyTrackService spotifyTrackService;

    @Autowired
    public FileService(ObjectMapper mapper, CustomObjectMapper customMapper, SpotifyAPIRequests requests, SpotifyUploadedFileService uploadedFileService, SpotifyTrackService spotifyTrackService){
        this.mapper = mapper;
        this.customMapper = customMapper;
        this.requests = requests;
        this.uploadedFileService = uploadedFileService;
        this.spotifyTrackService = spotifyTrackService;
    }

    //TODO: make method Async
    public void store(MultipartFile[] files) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        if(files.length == 0){
            throw new StorageException("No files provided");
        }

        for(MultipartFile file : files){
            if(file.isEmpty()){
                throw new StorageException("Failed to store empty file.");
            }
            String extension = Extension.getExtension(file.getOriginalFilename()).orElseThrow(()->
                    new StorageException("Unknown file extension."));

            switch(extension){
                case "zip" -> zipFile(file);
                case "json" -> jsonFile(file);
                default -> throw new StorageException("Unknown file extension.");
            }
        }
    }

    public void zipFile(MultipartFile file) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        try{
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry entry = zis.getNextEntry();

            while(entry != null){
                Optional<String> extensionOptional = Extension.getExtension(entry.getName());

                if(extensionOptional.isPresent()){
                    String entryExtension = extensionOptional.get();

                    if(entryExtension.equals("json")){

                        String json = new String(zis.readAllBytes(), StandardCharsets.UTF_8);

                        String entryName = entry.getName();
                        String filename = entryName.contains("/") ? entryName.substring(entryName.lastIndexOf("/")+1) : entryName;

                        resolveJson(json, filename);
                    }
                }
                entry = zis.getNextEntry();
            }
        }catch(IOException e){
            throw new StorageException("Failed to store file.");
        }
    }

    public void jsonFile(MultipartFile file) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        try{
            String json = new String(file.getBytes(), StandardCharsets.UTF_8);
            String filename = file.getOriginalFilename();

            resolveJson(json, filename);
        }catch(IOException e){
            throw new StorageException("Failed to store file.");
        }
    }


    private void resolveJson(String json, String filename) throws SpotifyNotConnectedException, SpotifyServerErrorException{
        try{
            SpotifyFileStreamDTO[] entities = mapper.readValue(json, SpotifyFileStreamDTO[].class);
            List<SpotifyFileStream> streams = new LinkedList<>(); //all streams
            HashSet<SpotifyTrack> uniqueTracks = new HashSet<>();

            for(SpotifyFileStreamDTO dto : entities){
                if(dto.getSpotifyTrackUri() != null){
                    SpotifyFileStream fileStream = customMapper.spotifyFileStreamDTOtoEntity(dto);
                    streams.add(fileStream);
                    uniqueTracks.add(new SpotifyTrack(fileStream.getSpotifyId()));
                }
            }

            HashMap<SpotifyTrack, List<SpotifyFileStream>> tracksStreams = getTracks(uniqueTracks);

            for(SpotifyFileStream stream : streams){
                SpotifyTrack streamSpotifyTrack = new SpotifyTrack(stream.getSpotifyId());
                tracksStreams.get(streamSpotifyTrack).add(stream);
            }

            for(Map.Entry<SpotifyTrack, List<SpotifyFileStream>> entry : tracksStreams.entrySet()){
                SpotifyTrack spotifyTrack = entry.getKey();
                spotifyTrack.getFileStreams().addAll(entry.getValue());
                spotifyTrackService.save(spotifyTrack);
            }
            uploadedFileService.save(filename, streams.size());

        }catch(JsonProcessingException e){
            throw new RuntimeException(e); //modified file
        }
    }

    private HashMap<SpotifyTrack, List<SpotifyFileStream>> getTracks(HashSet<SpotifyTrack> uniqueTracks) throws SpotifyNotConnectedException, SpotifyServerErrorException{
        HashMap<SpotifyTrack, List<SpotifyFileStream>> allTracks = new HashMap<>();
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        for(SpotifyTrack uniqueTrack : uniqueTracks){
            builder.append(uniqueTrack.getSpotifyId());
            counter++;

            if((counter % 50 == 0) || (uniqueTracks.size() == counter)){
                ResponseEntity<SeveralTracksDTO> response = requests.getSeveralTracks(builder.toString());
                builder = new StringBuilder();

                if(response.getBody() != null){
                    List<SpotifyTrackDTO> severalTracks = response.getBody().getTracks();

                    for(SpotifyTrackDTO dto : severalTracks){
                        System.out.println(dto.getName());
                        allTracks.put(customMapper.spotifyTrackDTOtoEntity(dto), new LinkedList<>());
                    }
                }else{
                    throw new SpotifyServerErrorException("Spotify server error");
                }
            }else{
                builder.append(",");
            }
        }

        return allTracks;
    }


}