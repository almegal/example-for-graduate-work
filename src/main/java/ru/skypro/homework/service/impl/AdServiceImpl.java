package ru.skypro.homework.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

@Service
public class AdServiceImpl implements AdService {
    @Override
    public AdsDto getAds() {
        return null;
    }

    @Override
    public AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile image) {
        return null;
    }

    @Override
    public ExtendedAdDto getExtendedAd(Integer id) {
        return null;
    }

    @Override
    public void removeAd(Integer id) {

    }

    @Override
    public AdDto updateAd(Integer id, AdDto ad) {
        return null;
    }

    @Override
    public List<AdDto> getAdsByAuthenticatedUser() {
        return List.of();
    }

    @Override
    public byte[] updateImageAd(Integer id, MultipartFile file) {
        return new byte[0];
    }
}
