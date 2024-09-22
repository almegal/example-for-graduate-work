package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.service.AdService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Api(tags = "Объявления", value = "API для работы с объявлениями")
public class AdsController {

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
    public ResponseEntity<AdsDto> getAds(Authentication authentication) {
        AdsDto ads = adsService.getAds(authentication);
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
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") CreateOrUpdateAdDto createAd,
                                       @RequestPart("image") MultipartFile image,
                                       Authentication authentication) throws IOException {
        AdDto createdAd = adsService.addAd(createAd, image, authentication);
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
    public ResponseEntity<ExtendedAdDto> getExtendedAd(@PathVariable Long id,
                                                       Authentication authentication) {
        ExtendedAdDto extendedAd = adsService.getExtendedAd(id, authentication);
        return ResponseEntity.ok(extendedAd);
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
    public ResponseEntity<Void> removeAd(@PathVariable Long id,
                                         Authentication authentication) throws IOException {
        adsService.removeAd(id, authentication);
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
    public ResponseEntity<AdDto> updateAd(@PathVariable Long id,
                                          @Valid @RequestBody CreateOrUpdateAdDto dto,
                                          Authentication authentication) {
        AdDto updateAd = adsService.updateAd(id, dto, authentication);
        return ResponseEntity.ok(updateAd);
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
    public ResponseEntity<AdsDto> getAdsByAuthenticatedUser(Authentication authentication) {
        AdsDto dto = adsService.getAdsByAuthenticatedUser(authentication);
        return ResponseEntity.ok(dto);
    }


    @ApiOperation(value = "Обновление картинки в объявлении",
            notes = "Позволяет обновить картинку в объявлении")
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
    public ResponseEntity<byte[]> updateImageAd(@PathVariable Long id,
                                                @Valid @RequestBody MultipartFile file,
                                                Authentication authentication) throws IOException {
        byte[] image = adsService.updateImageAd(id, file, authentication);
        return ResponseEntity.ok(image);
    }


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
    @GetMapping(value = "/image/{id}")
    public ResponseEntity<Void> downloadImage(@PathVariable Long id, HttpServletResponse response) {
        Ad ad = adsService.findAdById(id);
        if (ad.getImageUrl() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path path = Path.of(ad.getImageUrl());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            response.setStatus(200);
            response.setContentType(ad.getMediaType());
            response.setContentLength((int) ad.getFileSize());
            bis.transferTo(bos);
        } catch (IOException e) {
            log.error("Error uploading image file for ad with id = {}, path = {}",
                    ad.getId(), path, e);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
