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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageUploadService;

@RestController
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping
public class ImageController {
    private final ImageUploadService imageUploadService;

    @ApiOperation(value = "Выгрузка картинки из сервера в объявление",
            notes = "Позволяет выгрузить картинку из сервера в объявление")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping(value = "/image/{filePath}")
    public ResponseEntity<byte[]> downloadImage(
            @PathVariable String filePath) {
        byte[] image = imageUploadService.getImageByAdId("img/" + filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
