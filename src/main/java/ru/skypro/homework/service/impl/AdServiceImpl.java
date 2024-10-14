package ru.skypro.homework.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.UploadImage;

/**
 * Класс по работе с объявлениями
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserService userService;
    private final SecurityServiceImpl securityService;

    @Override
    public AdsDto getAds() {
        List<AdDto> ads = adMapper.toDtos(adRepository.findAll());
        return AdsDto.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    @Override
    @Transactional
    public AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile file) {
        Ad ad = new Ad();

        String username = securityService.getAuthenticatedUserName();
        User user = userService.getUserByEmailFromDb(username);

        adMapper.updateAdFromUpdateAdDto(createAd, ad);
        ad.setAuthor(user);
        try {
            final String urlImage = UploadImage.uploadImage(file);
            String newUrlImage = urlImage.replace("/", "");
            ad.setImageUrl(newUrlImage);
            // В сущность Ad сохраняется путь к файлу, состоящий только из имени файла (без имени папки и "/")

        } catch (IOException e) {
            log.error("Error uploading image file path = {}", ad.getImageUrl(), e);
            throw new RuntimeException(e);
        }
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Override
    public ExtendedAdDto getExtendedAd(Long id) {
        Ad ad = findAdById(id);
        return adMapper.toExtendedDto(ad);
    }

    @Override
    @PreAuthorize("@adServiceImpl.isAdCreatorOrAdmin(#id)")
    public void removeAd(Long id) throws IOException {
        Ad ad = findAdById(id);
        Path path = Path.of("images/" + ad.getImageUrl());
        Files.deleteIfExists(path);
        adRepository.deleteById(ad.getId());
    }

    @Override
    @PreAuthorize("@adServiceImpl.isAdCreatorOrAdmin(#id)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public AdDto updateAd(Long id, CreateOrUpdateAdDto dto) {
        Ad ad = findAdById(id);
        adMapper.updateAdFromUpdateAdDto(dto, ad);
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Override
    public AdsDto getAdsByAuthenticatedUser() {
        String username = securityService.getAuthenticatedUserName();
        User user = userService.getUserByEmailFromDb(username);

        List<Ad> ads = adRepository.findAllByUserId(user.getId());
        List<AdDto> adDtos = adMapper.toDtos(ads);

        return AdsDto.builder()
                .count(ads.size())
                .results(adDtos)
                .build();
    }

    @Override
    @PreAuthorize("@adServiceImpl.isAdCreatorOrAdmin(#id)")
    @Transactional
    public AdDto updateImageAd(Long id, MultipartFile file) {
        Ad ad = findAdById(id);
        try {
            String urlImage = UploadImage.uploadImage(file).replace("/", "");
            ad.setImageUrl(urlImage);
            // В сущность Ad сохраняется путь к файлу, состоящий только из имени файла (без имени папки и "/")

        } catch (IOException e) {
            log.error("Error uploading image file path = {}", ad.getImageUrl(), e);
            throw new RuntimeException(e);
        }
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Override
    public Ad findAdById(Long id) {
        return adRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
    }

    @Override
    public boolean isAdCreatorOrAdmin(Long id) {
        Ad ad = findAdById(id);
        String email = securityService.getAuthenticatedUserName();
        User user = userService.getUserByEmailFromDb(email);
        return user.getRole() == Role.ADMIN || email.equals(ad.getAuthor().getEmail());
    }
}
