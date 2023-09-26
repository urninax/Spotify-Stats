package me.urninax.spotifystats.references.internal.controllers;

import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageException;
import me.urninax.spotifystats.references.internal.uploading.responses.UploadErrorResponse;
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
        UploadErrorResponse uploadErrorResponse = new UploadErrorResponse(
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(uploadErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exc, WebRequest request){
        UploadErrorResponse uploadErrorResponse = new UploadErrorResponse(
                Instant.now(),
                String.format("%s. Maximum file size is %s", exc.getMessage(), maxSize),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(uploadErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
