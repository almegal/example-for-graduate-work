package ru.skypro.homework.util;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageUploaderException;

@Slf4j
public class UploadImage {

    public static String uploadImage(MultipartFile imageFile)
            throws ImageUploaderException, IOException {
        String idImage = UUID.randomUUID().toString();
        Path filePath = Path.of("img", idImage + "." + getExtension(
                Objects.requireNonNull(imageFile.getOriginalFilename())));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            log.error("Error uploading image file path = {}",
                    filePath, e);
            throw new ImageUploaderException(e.getMessage());
        }
        return filePath.toString();
    }

    static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
