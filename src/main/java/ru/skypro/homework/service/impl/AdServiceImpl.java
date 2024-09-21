package ru.skypro.homework.service.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Value("${path.to.photos.folder}")
    private String photoDir;

    @Override
    public AdsDto getAds(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        List<AdDto> ads = adMapper.toDtos((List<Ad>) adRepository.findAll());
        return new AdsDto(ads.size(), ads);
    }

    @Override
    public AdDto addAd(CreateOrUpdateAdDto createAd,
                       MultipartFile image,
                       Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = new Ad();
        adMapper.updateAdFromUpdateAdDto(createAd, ad);
        User user = userRepository.findUserByEmail(authentication.getName());
        ad.setAuthor(user);
        adRepository.save(ad);

        uploadImageForAd(ad, image);
        return adMapper.toDto(ad);
    }

    @Override
    public ExtendedAdDto getExtendedAd(Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        return adMapper.toExtendedDto(ad);
    }

    @Override
    public void removeAd(Long id, Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        Path path = Path.of(ad.getFilePath());
        Files.deleteIfExists(path);
        adRepository.deleteById(ad.getId());
    }

    @Override
    public AdDto updateAd(Long id, CreateOrUpdateAdDto dto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Override
    public AdsDto getAdsByAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        List<AdDto> ads = adRepository.findAll().stream()
                .filter(ad -> (ad.getAuthor().getEmail()).equals(authentication.getName()))
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return new AdsDto(ads.size(), ads);
    }

    @Override
    public byte[] updateImageAd(Long id, MultipartFile file, Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        uploadImageForAd(ad, file);
        return Files.readAllBytes(Path.of(ad.getFilePath()));
    }

    private void uploadImageForAd(Ad ad, MultipartFile imageFile) throws IOException {
        Path filePath = Path.of(photoDir, ad.getId() + "." + getExtension(imageFile.getOriginalFilename()));
        System.out.println(filePath);

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            log.info("Converting bytes ....");
            bis.transferTo(bos);

        } catch (IOException e) {
        log.error("Error uploading image file for ad with id = {}, path = {}",  ad.getId(), filePath, e);
    }

        log.info("File has been uploaded!");

        ad.setFilePath(filePath.toString());
        ad.setFileSize(imageFile.getSize());
        ad.setMediaType(imageFile.getContentType());

        adRepository.save(ad);
        log.info("ImageFile has been saved! id = {}, path = {}", ad.getId(), filePath);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
