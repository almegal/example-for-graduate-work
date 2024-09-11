package ru.skypro.homework.service;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

public interface AdService {
    public AdsDto getAds();

    AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile image);

    ExtendedAdDto getExtendedAd(Integer id);

    void removeAd(Integer id);

    AdDto updateAd(Integer id, @Valid AdDto ad);

    List<AdDto> getAdsByAuthenticatedUser();

    byte[] updateImageAd(Integer id, @Valid MultipartFile file);
}
