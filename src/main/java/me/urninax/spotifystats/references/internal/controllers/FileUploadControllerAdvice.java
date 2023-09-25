package me.urninax.spotifystats.references.internal.controllers;

import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageException;
import me.urninax.spotifystats.references.internal.uploading.responses.UploadErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class FileUploadControllerAdvice extends ResponseEntityExceptionHandler{

    @ExceptionHandler( {StorageException.class} )
    public ResponseEntity<?> handleStorageException(StorageException exc, WebRequest request){
        UploadErrorResponse uploadErrorResponse = new UploadErrorResponse(
                Instant.now(),
                exc.getMessage(),
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(uploadErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
