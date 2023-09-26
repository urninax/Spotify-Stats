package me.urninax.spotifystats.references.internal.uploading;

import me.urninax.spotifystats.security.models.User;
import org.springframework.core.io.Resource;
import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageException;
import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{
    private final Path zipLocation;
    private final Path jsonLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties){
        this.zipLocation = Paths.get(properties.getZipLocation());
        this.jsonLocation = Paths.get(properties.getJsonLocation());
    }

    @Override
    public void store(MultipartFile file, User user){
        try{
            Path destinationFile = formDestinationFile(file, user);

            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            //TODO: unzip if zip, read if json
            //TODO: move data to database
            //TODO: delete uploaded files

        }catch(IOException e){
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll(){
        try{
            return Files.walk(this.jsonLocation, 1)
                    .filter(path -> !path.equals(this.jsonLocation))
                    .map(this.jsonLocation::relativize);
        }catch(IOException e){
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename){
        return jsonLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename){
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(Path userDirectory){
        FileSystemUtils.deleteRecursively(userDirectory.toFile());
    }

    public Path formDestinationFile(MultipartFile file, User user){
        if(file.isEmpty()){
            throw new StorageException("Failed to store empty file.");
        }

        try{
            String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];

            if(extension.equals("zip")){
                return zipLocation.resolve(String.format("%s.%s", user.getId(), extension));
            }else if(extension.equals("json")){
                Path userCreatedDirectory = Files.createDirectory(jsonLocation.resolve(String.valueOf(user.getId())));
                return userCreatedDirectory.resolve(file.getOriginalFilename());
            }else{
                throw new StorageException("Unknown file extension.");
            }
        }catch(ArrayIndexOutOfBoundsException exc){
            throw new StorageException("Unknown file extension.");
        }catch(IOException e){
            throw new StorageException("Failed to store file.", e);
        }
    }
}
