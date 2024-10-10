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

/**
 * Класс, создающий логику по выгрузке любых картинок из файловой системы
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

    private final AdService adService;

    @Override
    public byte[] getImageByAdId(String urlPath) {

        Path path = Path.of("images/" + urlPath);
        // В конструктор объекта типа Path передаем путь к файлу, состоящий из имени папки, "/" и имени файла,
        // извлеченного из сущностей Ad или User

        byte[] image;
        try (InputStream is = Files.newInputStream(path);
             ByteArrayOutputStream os = new ByteArrayOutputStream(1024)) {

            is.transferTo(os);
            image = os.toByteArray();
            log.info("File with path = {} uploaded successfully!", path);

        } catch (IOException ex) {
            log.error("Error downloading image file for ad with path = {}", path, ex);
            throw new RuntimeException(ex.getMessage());
        }
        return image;
    }
}
