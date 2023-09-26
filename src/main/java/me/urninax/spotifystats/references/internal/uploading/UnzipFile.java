package me.urninax.spotifystats.references.internal.uploading;

import me.urninax.spotifystats.references.internal.uploading.exceptions.StorageException;
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
    public static void unzip(MultipartFile file, Path path){
        try{
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            byte[] buffer = new byte[1024];

            ZipEntry entry = zis.getNextEntry();

            while(entry != null){
                String entryExtension = Extension.getExtension(entry.getName()).orElseThrow(() ->
                        new StorageException("Unknown file extension."));

                if(entry.getName().startsWith("endsong") && entryExtension.equals("json")){
                    FileOutputStream fos = getFileOutputStream(path, entry);
                    int len;
                    while((len = zis.read(buffer))>0){
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    entry = zis.getNextEntry();
                }
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private static FileOutputStream getFileOutputStream(Path path, ZipEntry entry) throws FileNotFoundException{
        Path destinationFile = path.resolve(entry.getName()).toAbsolutePath();

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
