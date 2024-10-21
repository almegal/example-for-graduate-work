package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageUploadService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Api(tags = "Объявления", value = "API для работы с объявлениями")
public class AdController {

    private final AdService adsService;


    @ApiOperation(value = "Получение всех объявлений",
            notes = "Возвращает общее количество и список всех объявлений",
            response = AdsDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping
    public AdsDto getAds() {
        return adsService.getAds();
    }


    @ApiOperation(value = "Добавление объявления",
            notes = "Позволяет добавить объявление",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(
            @Valid @RequestPart("properties") CreateOrUpdateAdDto createAd,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAd(createAd, image));
    }


    @ApiOperation(value = "Получение полного объявления",
            notes = "Позволяет получить полное объявление по его идентификатору",
            response = ExtendedAdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping("/{id}")
    public ExtendedAdDto getExtendedAd(@PathVariable Long id) {
        return adsService.getExtendedAd(id);
    }


    @ApiOperation(value = "Удаление объявления",
            notes = "Позволяет удалить объявление по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "No Content"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Long id) throws IOException {
        adsService.removeAd(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Обновление информации об объявлении",
            notes = "Позволяет обновить информацию в объявлении",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @PatchMapping("/{id}")
    public AdDto updateAd(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateAdDto ad) {
        return adsService.updateAd(id, ad);
    }

    @ApiOperation(value = "Получение объявлений авторизованного пользователя",
            notes = "Возвращает список объявлений авторизованного пользователя",
            response = AdsDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @GetMapping("/me")
    public AdsDto getAdsByAuthenticatedUser() {
        return adsService.getAdsByAuthenticatedUser();
    }


    @ApiOperation(value = "Обновление картинки в объявлении",
            notes = "Позволяет обновить картинку в объявлении",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDto updateImageAd(@PathVariable Long id, @RequestPart("image") MultipartFile file) {
        return adsService.updateImageAd(id, file);
    }
}
