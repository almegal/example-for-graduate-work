package ru.skypro.homework.util;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * Класс, реализующий логику по скачиванию файла (картинки) и сохранению его в файловую систему 
 */
@Slf4j
public class UploadImage {

    /**
     * Метод по загрузке файла в файловую систему и сохранению пути к файлу (имени файла без имени папки и "/")
     * в сущности Ad и User
     * Метод использует:
     * {@link java.nio.file.Path#of(String, String...)}
     * {@link MultipartFile#getOriginalFilename()}
     * {@link java.nio.file.Files#createDirectories(Path, FileAttribute[])}
     * {@link java.nio.file.Files#deleteIfExists(Path)}
     * {@link MultipartFile#getInputStream()}
     * {@link java.nio.file.Files#newOutputStream(Path, OpenOption...)}
     * {@link java.io.InputStream#transferTo(OutputStream)}
     *
     * @param imageFile - загружаемая для объявления или пользователя картинка
     * @return путь к файлу (без имени папки и "/")
     * @throws IOException - исключение, выбрасываемое при загрузке файла
     */
    public static String uploadImage(MultipartFile imageFile) throws IOException {
        String idImage = UUID.randomUUID().toString();
        Path filePath = Path.of("images", idImage + "." + getExtension(
                Objects.requireNonNull(imageFile.getOriginalFilename())));
        // Создаем путь для хранения картинки на диске, состоящий из имени папки и имени файла

        Files.createDirectories(filePath);
        // Создаем папку для хранения файлов по созданному пути
        Files.deleteIfExists(filePath);
        // Если по созданному пути уже существует файл, то удаляем его

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
            log.info("File with path = {} uploaded successfully!", filePath);

        } catch (IOException e) {
            log.error("Error uploading image file path = {}", filePath, e);
            throw new RuntimeException(e.getMessage());
        }
        return "/" + idImage + "." + getExtension(imageFile.getOriginalFilename());
        // Данный метод возвращает только "/" и имя файла. В сущность Ad будет сохранен путь, состоящий только из имени
        // файла (без имени папки и "/"), а в сущность User - путь, состоящий из "/" и имени файла (без имени папки)
    }

    static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
