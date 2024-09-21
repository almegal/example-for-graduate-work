package ru.skypro.homework.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

public class AdMapperTest {

    private final Ad AD_ENTITY = adGenerator();
    private final AdDto AD_DTO = adDtoGenerator();
    private final List<Ad> ADS_ENTITY = listAdsGenerator();
    private final List<AdDto> ADS_DTO = listAdsDtoGenerator();
    private final ExtendedAdDto EXTENDED_AD_DTO = extendedAdDtoGenerator();
    private final CreateOrUpdateAdDto CREATE_OR_UPDATE_AD_DTO = createOrUpdateAdDtoGenerator();
    private final AdMapper mapper = AdMapper.INSTANCE;


    @Test
    @DisplayName("Корректно маппится из Ad в AdDto")
    public void shouldCorrectConvertFromEntityAdToDtoWithCorrectValue() {
        AdDto expected = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image(AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();

        AdDto actual = mapper.toDto(AD_ENTITY);
        assertNotNull(actual);
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getPk(), actual.getPk());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    @DisplayName("Корректно маппится из List<Ad> ads в List<AdDto> adsDto")
    public void shouldCorrectConvertFromEntityAdsToDtoWithCorrectValue() {
        AdDto adDto1 = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image(AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
        AdDto adDto2 = AdDto.builder()
                .author(AD_AUTHOR_ID_2)
                .image(AD_IMAGE_2)
                .pk(AD_ID_2)
                .price(AD_PRICE_2)
                .title(AD_TITLE_2)
                .build();
        List<AdDto> expected = List.of(adDto1, adDto2);

        List<AdDto> actual = mapper.toDtos(ADS_ENTITY);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("Корректно маппится из Ad в ExtendedAdDto")
    public void shouldCorrectConvertFromEntityAdToExtendedAdDtoWithCorrectValue() {
        ExtendedAdDto expected = ExtendedAdDto.builder()
                .pk(AD_ID_1)
                .description(AD_DESCRIPTION_1)
                .image(AD_IMAGE_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .email(AD_AUTHOR_EMAIL_1)
                .authorFirstName(AD_AUTHOR_FIRST_NAME_1)
                .authorLastName(AD_AUTHOR_LAST_NAME_1)
                .phone(AD_AUTHOR_PHONE_1)
                .build();

        ExtendedAdDto actual = mapper.toExtendedDto(AD_ENTITY);
        assertNotNull(actual);
        assertEquals(expected.getPk(), actual.getPk());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getAuthorFirstName(), actual.getAuthorFirstName());
        assertEquals(expected.getAuthorLastName(), actual.getAuthorLastName());
        assertEquals(expected.getPhone(), actual.getPhone());
    }

    @Test
    @DisplayName("Корректно обновляет данные из CreateOrUpdateAdDto в Ad")
    public void shouldCorrectMapFromCreateOrUpdateAdDtoToAd() {
        Ad expected = adGenerator();
        expected.setTitle(AD_NEW_TITLE_1);
        expected.setPrice(AD_NEW_PRICE_1);
        expected.setDescription(AD_NEW_DESCRIPTION_1);

        Ad actual = adGenerator();
        mapper.updateAdFromUpdateAdDto(CREATE_OR_UPDATE_AD_DTO, actual);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getFilePath(), actual.getFilePath());
        assertEquals(expected.getFileSize(), actual.getFileSize());
        assertEquals((expected.getMediaType()), actual.getMediaType());
        assertEquals(expected.getAuthor(), actual.getAuthor());
    }


}
