package me.urninax.spotifystats.components.controllers;

import me.urninax.spotifystats.components.utils.GlobalResponse;
import me.urninax.spotifystats.functions.uploading.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;

@RestControllerAdvice
public class StorageControllerAdvice{
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSize;

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<?> handleStorageException(StorageException exc, WebRequest request){
        GlobalResponse response = new GlobalResponse(
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exc, WebRequest request){
        GlobalResponse response = new GlobalResponse(
                Instant.now(),
                String.format("%s. Maximum file size is %s", exc.getMessage(), maxSize),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }
}
