package ru.skypro.homework.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * Сервис, создающий логику по выгрузке картинок из файловой системы
 */
public interface ImageUploadService {

    /**
     * Получение картинки по пути к файлу, извлеченному из объектов Ad или User, к которому добавляется имя папки и "/"
     * Метод использует:
     * {@link java.nio.file.Path#of(String, String...)}
     * {@link java.nio.file.Files#newInputStream(Path, OpenOption...)}
     * {@link java.io.InputStream#transferTo(OutputStream)}
     * {@link ByteArrayOutputStream#toByteArray()}
     * @param urlPath - имя файла (без имени папки и "/")
     * @return картинку в виде массива байтов
     */
    byte[] getImageByAdId(String urlPath);
}
