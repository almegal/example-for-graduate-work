package ru.skypro.homework.service;

import java.io.IOException;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

public interface AdService {
    public AdsDto getAds();

    AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile image);

    ExtendedAdDto getExtendedAd(Long id);

    void removeAd(Long id) throws IOException;

    AdDto updateAd(Long id, @Valid CreateOrUpdateAdDto ad);

    AdsDto getAdsByAuthenticatedUser();

    AdDto updateImageAd(Long id,
                        @Valid MultipartFile file);

    Ad findAdById(Long id);

//    public boolean isAdCreatorOrAdmin(Long id);
}
