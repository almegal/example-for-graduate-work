package ru.skypro.homework.service;

import java.io.IOException;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

public interface AdService {
    AdsDto getAds(Authentication authentication);

    AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile image, Authentication authentication) throws IOException;

    ExtendedAdDto getExtendedAd(Long id, Authentication authentication);

    void removeAd(Long id, Authentication authentication) throws IOException;

    AdDto updateAd(Long id, @Valid CreateOrUpdateAdDto dto, Authentication authentication);

    AdsDto getAdsByAuthenticatedUser(Authentication authentication);

    byte[] updateImageAd(Long id, @Valid MultipartFile file, Authentication authentication) throws IOException;

    Ad findAdById(Long id);
}
