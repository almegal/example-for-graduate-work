package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdsService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Api(tags = "Объявления", value = "API для работы с объявлениями")
public class AdsController {

    private final AdsService adsService;


    @ApiOperation(value = "Получение всех объявлений",
            notes = "Возвращает список всех объявлений",
            response = AdDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping
    public ResponseEntity<List<AdDto>> getAllAds() {
        List<AdDto> ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }


    @ApiOperation(value = "Добавление объявления",
            notes = "Позволяет добавить объявление",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(
            @RequestPart("properties") CreateOrUpdateAdDto createAd,
            @RequestPart("image") MultipartFile image) {
        AdDto createdAd = adsService.addAd(createAd, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
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
    public ResponseEntity<ExtendedAdDto> getExtendedAd(@PathVariable Integer id) {
        ExtendedAdDto extendedAd = adsService.getExtendedAd(id);
        return ResponseEntity.ok(extendedAd);
    }


    @ApiOperation(value = "Удаление объявления",
            notes = "Позволяет удалить объявление по его идентификатору"
    )
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
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        adsService.removeAd(id);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(value = "Обновление информации об объявлении",
            notes = "Позволяет обновить информацию в объявлении",
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
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable Integer id, @Valid @RequestBody AdDto ad) {
        AdDto updateAd = adsService.updateAd(id, ad);
        return ResponseEntity.ok(updateAd);
    }


    @ApiOperation(value = "Получение объявлений авторизованного пользователя",
            notes = "Возвращает список объявлений авторизованного пользователя",
            response = AdDto.class,
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
    public ResponseEntity<List<AdDto>> getAdsByAuthenticatedUser() {
        List<AdDto> ads = adsService.getAdsByAuthenticatedUser();
        return ResponseEntity.ok(ads);
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
    public ResponseEntity<byte[]> updateImageAd(@PathVariable Integer id, @Valid @RequestBody MultipartFile file) {
        byte[] image  = adsService.updateImageAd(id, file);
        return ResponseEntity.ok(image);
    }


}
