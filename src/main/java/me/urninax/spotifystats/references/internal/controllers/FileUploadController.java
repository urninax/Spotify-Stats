package me.urninax.spotifystats.references.internal.controllers;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.references.internal.uploading.services.StorageService;
import me.urninax.spotifystats.references.internal.uploading.responses.UploadedResponse;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/settings")
@AllArgsConstructor
public class FileUploadController{
    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        storageService.store(file, userDetails.getUser());

        UploadedResponse uploadedResponse = new UploadedResponse("File successfully uploaded", Instant.now());

        return new ResponseEntity<>(uploadedResponse, HttpStatus.ACCEPTED);
    }
}
