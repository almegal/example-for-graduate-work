package ru.skypro.homework.service.impl;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;

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
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        List<AdDto> ads = adMapper.toDtos(adRepository.findAll());
        return AdsDto.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    @Override
    @Transactional
    public AdDto addAd(CreateOrUpdateAdDto createAd,
                       MultipartFile image,
                       Authentication authentication) throws IOException {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = new Ad();
        adMapper.updateAdFromUpdateAdDto(createAd, ad);
        User user = userRepository
                .findByEmail(authentication.getName()).orElseThrow();
        ad.setAuthor(user);
        adRepository.save(ad);

        uploadImageForAd(ad, image);
        return adMapper.toDto(ad);
    }

    @Override
    public ExtendedAdDto getExtendedAd(Long id, Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = findAdById(id);
        return adMapper.toExtendedDto(ad);
    }

    @Override
    public void removeAd(Long id, Authentication authentication) throws IOException {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = findAdById(id);
        Path path = Path.of(ad.getImageUrl());
        if (isAdCreatorOrAdmin(ad, authentication)) {
            Files.deleteIfExists(path);
            adRepository.deleteById(ad.getId());
        } else {
            throw new ForbiddenException("Отсутствуют права на операции с объявлением");
        }
    }

    @Override
    public AdDto updateAd(Long id, CreateOrUpdateAdDto dto, Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = findAdById(id);
        if (isAdCreatorOrAdmin(ad, authentication)) {
            ad.setTitle(dto.getTitle());
            ad.setPrice(dto.getPrice());
            ad.setDescription(dto.getDescription());
            adRepository.save(ad);
            return adMapper.toDto(ad);
        } else {
            throw new ForbiddenException("Отсутствуют права на операции с объявлением");
        }
    }

    @Override
    public AdsDto getAdsByAuthenticatedUser(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        List<AdDto> ads = adRepository.findAll().stream()
                .filter(ad -> (ad.getAuthor().getEmail()).equals(authentication.getName()))
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return AdsDto.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    @Override
    public byte[] updateImageAd(Long id,
                                MultipartFile file,
                                Authentication authentication) throws IOException {
        if (!isAuthenticated(authentication)) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        Ad ad = findAdById(id);
        if (isAdCreatorOrAdmin(ad, authentication)) {
            uploadImageForAd(ad, file);
            return Files.readAllBytes(Path.of(ad.getImageUrl()));
        } else {
            throw new ForbiddenException("Отсутствуют права на операции с объявлением");
        }
    }

    public Ad findAdById(Long id) {
        return adRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
    }

    private void uploadImageForAd(Ad ad, MultipartFile imageFile) throws IOException {
        Path filePath = Path.of(photoDir, ad.getId() + "." + getExtension(
                Objects.requireNonNull(imageFile.getOriginalFilename())));
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
            log.error("Error uploading image file for ad with id = {}, path = {}",
                    ad.getId(), filePath, e);
        }

        log.info("File has been uploaded!");

        ad.setImageUrl(filePath.toString());
        ad.setFileSize(imageFile.getSize());
        ad.setMediaType(imageFile.getContentType());

        adRepository.save(ad);
        log.info("ImageFile has been saved! id = {}, path = {}", ad.getId(), filePath);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isAdCreatorOrAdmin(Ad ad, Authentication authentication) {
        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow()
                .getRole() == Role.ADMIN ||
                authentication.getName().equals(ad.getAuthor().getEmail());
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

}
