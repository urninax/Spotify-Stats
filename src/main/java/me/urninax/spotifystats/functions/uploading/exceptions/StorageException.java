package me.urninax.spotifystats.functions.uploading.exceptions;

public class StorageException extends RuntimeException{
    public StorageException(String message){
        super(message);
    }
}
