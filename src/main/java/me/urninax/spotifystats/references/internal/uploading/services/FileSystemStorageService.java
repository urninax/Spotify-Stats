package me.urninax.spotifystats.references.internal.uploading.services;

import me.urninax.spotifystats.references.internal.uploading.StorageProperties;
import me.urninax.spotifystats.references.internal.uploading.utils.Extension;
import me.urninax.spotifystats.references.internal.uploading.utils.UnzipFile;
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
    private final Path filesLocation;
    private final UnzipFile unzipFile;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, UnzipFile unzipFile){
        this.filesLocation = Paths.get(properties.getFilesLocation());
        this.unzipFile = unzipFile;
    }

    @Override
    public void store(MultipartFile file, User user){
        if(file.isEmpty()){
            throw new StorageException("Failed to store empty file.");
        }

        try{
            String extension = Extension.getExtension(file.getOriginalFilename()).orElseThrow(()->
                    new StorageException("Unknown file extension."));

            Path userCreatedDirectory = filesLocation.resolve(String.valueOf(user.getId()));

            if(!Files.exists(userCreatedDirectory)){
                Files.createDirectory(userCreatedDirectory);
            }

            switch(extension){
                case "zip" -> unzipFile.unzip(file, userCreatedDirectory);
                case "json" -> {
                    Path destinationFile = userCreatedDirectory.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                    try(InputStream inputStream = file.getInputStream()){
                        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                default -> throw new StorageException("Unknown file extension.");
            }

        }catch(IOException e){
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll(){
        try{
            return Files.walk(this.filesLocation, 1)
                    .filter(path->!path.equals(this.filesLocation))
                    .map(this.filesLocation::relativize);
        }catch(IOException e){
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename){
        return filesLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename){
        try{
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new StorageFileNotFoundException(
                        "Could not read file: "+filename);

            }
        }catch(MalformedURLException e){
            throw new StorageFileNotFoundException("Could not read file: "+filename, e);
        }
    }

    @Override
    public void deleteAll(Path userDirectory){
        FileSystemUtils.deleteRecursively(userDirectory.toFile());
    }

}
