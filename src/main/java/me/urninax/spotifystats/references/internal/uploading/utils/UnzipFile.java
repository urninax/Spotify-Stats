package me.urninax.spotifystats.references.internal.uploading.utils;

import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageException;
import me.urninax.spotifystats.references.internal.uploading.utils.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class UnzipFile{
    public void unzip(MultipartFile file, Path path) throws StorageException{ //file -> zip file, path -> files/{user_id}
        try{
            byte[] buffer = new byte[1024];

            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry entry = zis.getNextEntry();

            while(entry != null){
                Optional<String> extensionOptional = Extension.getExtension(entry.getName());
                if(extensionOptional.isPresent()){
                    String entryExtension = extensionOptional.get();
                    if(entry.getName().contains("endsong") && entryExtension.equals("json")){
                        FileOutputStream fos = getFileOutputStream(path, entry);
                        int len;
                        while((len = zis.read(buffer))>0){
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                }
                entry = zis.getNextEntry();
            }
        }catch(IOException e){
            throw new StorageException("Failed to store file.");
        }
    }

    private static FileOutputStream getFileOutputStream(Path path, ZipEntry entry) throws FileNotFoundException, StorageException{
        String entryName = entry.getName();
        String filename = entryName.contains("/") ? entryName.substring(entryName.lastIndexOf("/") + 1) : entryName;

        Path destinationFile = path.resolve(filename).toAbsolutePath();

        if(!destinationFile.getParent().equals(path.toAbsolutePath())){
            // This is a security check
            throw new StorageException(
                    "Cannot store file outside current directory.");
        }

        File parent = destinationFile.toFile().getParentFile();
        if(!parent.isDirectory() && !parent.mkdirs()){
            throw new StorageException("Failed to store file.");
        }

        return new FileOutputStream(destinationFile.toFile());
    }
}
