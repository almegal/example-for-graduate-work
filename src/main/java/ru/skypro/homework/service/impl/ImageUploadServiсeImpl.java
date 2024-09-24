package ru.skypro.homework.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageUploadService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploadServiсeImpl implements ImageUploadService {
    private final AdService adService;

    @Override
    public byte[] getImageByAdId(String urlPath) {
        Path path = Path.of(urlPath);
        try (InputStream is = Files.newInputStream(path);
             ByteArrayOutputStream os = new ByteArrayOutputStream(1024)) {

            is.transferTo(os);
            return os.toByteArray();
        } catch (IOException ex) {
            log.error("Error downloading image file for ad with path = {}",
                    path, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
