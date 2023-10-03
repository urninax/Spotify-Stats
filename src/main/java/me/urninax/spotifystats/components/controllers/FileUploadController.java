package me.urninax.spotifystats.components.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.components.utils.GlobalResponse;
import me.urninax.spotifystats.references.internal.uploading.services.FileService;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyNotConnectedException;
import me.urninax.spotifystats.spotifyauth.utils.exceptions.SpotifyServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/settings")
@AllArgsConstructor
public class FileUploadController{
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("files") MultipartFile[] files, WebRequest request) throws SpotifyServerErrorException, SpotifyNotConnectedException{
        fileService.store(files);

        GlobalResponse response = new GlobalResponse(Instant.now(), "File successfully uploaded", request.getDescription(false).substring(4));

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
