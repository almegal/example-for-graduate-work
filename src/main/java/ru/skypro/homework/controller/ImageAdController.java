package ru.skypro.homework.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageUploadService;

@RestController
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageAdController {

    private final ImageUploadService imageUploadService;


    @ApiOperation(value = "Выгрузка картинки из файловой системы в объявление",
            notes = "Позволяет выгрузить картинку из файловой системы")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping(value = "/image/{filePath}")
    public ResponseEntity<byte[]> downloadImageForAd(@PathVariable String filePath) {
        byte[] image = imageUploadService.getImageByAdId(filePath);
        // filePath, извлеченный из Ad, содержит только имя файла (без имени папки и "/")
        // в методе сервиса к нему добавляется имя папки и "/"
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    // Для того чтобы получить картинку из файловой системы, фронтенд обращается по адресу: localhost:8080/image
    // и в качестве переменной передает в пути еще и имя файла картинки (путь к файлу без имени папки и "/").
    // Мы удаляем имя папки из пути к файлу по той причине, чтобы при подставлении фронтендом значения "путь
    // к файлу" в путь localhost:8080/image, избежать получения эндпоинта: /image/images, которого в действительности нет.

}
