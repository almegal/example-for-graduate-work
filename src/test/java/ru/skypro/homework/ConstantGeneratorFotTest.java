package ru.skypro.homework;

import java.util.List;
import java.util.Random;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;

public class ConstantGeneratorFotTest {
    public static final Long USER_ID = new Random().nextLong();
    public static final String USER_EMAIL = "somecool@mail.com";
    public static final String USER_FIRST_NAME = "Jonh";
    public static final String USER_LAST_NAME = "Show";
    public static final String USER_PHONE = "9112223344";
    public static final Role USER_ROLE = Role.USER;
    public static final String USER_IMAGE = "Строка или массив байтов блеать?";
    public static final String USER_PASSWORD = "supersecretpassword";
    public static final String NEW_USER_EMAIL = "dontcool@mail.com";
    public static final String NEW_USER_FIRST_NAME = "Farhod";
    public static final String NEW_USER_LAST_NAME = "Blackwood";
    public static final String NEW_USER_PHONE = "9112443344";
    public static final Role NEW_USER_ROLE = Role.ADMIN;
    public static final String NEW_USER_IMAGE = "Строка блеать?";
    public static final String NEW_USER_PASSWORD = "notsecretpassword";

    public static final Long AD_ID_1 = new Random().nextLong();
    public static final String AD_TITLE_1 = "Title 1";
    public static final String AD_DESCRIPTION_1 = "Description 1";
    public static final Integer AD_PRICE_1 = 5000;
    public static final String AD_IMAGE_1 = "/Users/alex/Desktop/photos_2/1.jpg";;
    public static final int AD_FILE_SIZE_1 = 24_440;
    public static final String AD_MEDIA_TYPE_1 = "Media type 1";
    public static final User AD_AUTHOR_1 = userGenerator();
    public static final Long AD_AUTHOR_ID_1 = AD_AUTHOR_1.getId();
    public static final String AD_AUTHOR_EMAIL_1 = AD_AUTHOR_1.getEmail();
    public static final String AD_AUTHOR_FIRST_NAME_1 = AD_AUTHOR_1.getFirstName();
    public static final String AD_AUTHOR_LAST_NAME_1 = AD_AUTHOR_1.getLastName();
    public static final String AD_AUTHOR_PHONE_1 = AD_AUTHOR_1.getPhone();
    public static final String AD_NEW_TITLE_1 = "New title 1";
    public static final Integer AD_NEW_PRICE_1 = 4000;
    public static final String AD_NEW_DESCRIPTION_1 = "New description 1";

    public static final Long AD_ID_2 = new Random().nextLong();
    public static final String AD_TITLE_2 = "Title 2";
    public static final String AD_DESCRIPTION_2 = "Description 2";
    public static final Integer AD_PRICE_2 = 6000;
    public static final String AD_IMAGE_2 = "/Users/alex/Desktop/photos_2/2.jpg";;
    public static final int AD_FILE_SIZE_2 = 30_440;
    public static final String AD_MEDIA_TYPE_2 = "Media type 2";
    public static final User AD_AUTHOR_2 = userGenerator();
    public static final Long AD_AUTHOR_ID_2 = AD_AUTHOR_2.getId();


    public static User userGenerator() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setPhone(USER_PHONE);
        user.setRole(USER_ROLE);
        user.setImage(USER_IMAGE);
        user.setPassword(USER_PASSWORD);
        return user;
    }

    public static UserDto userDtoGenerator() {
        return UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .phone(USER_PHONE)
                .image(USER_IMAGE)
                .build();
    }

    public static UpdateUserDto updateUserDtoGenerator() {
        return UpdateUserDto.builder()
                .phone(NEW_USER_PHONE)
                .firstName(NEW_USER_FIRST_NAME)
                .lastName(NEW_USER_LAST_NAME)
                .build();
    }

    public static RegisterDto registerDtoGenerator() {
        return RegisterDto.builder()
                .role(NEW_USER_ROLE)
                .password(NEW_USER_PASSWORD)
                .username(NEW_USER_EMAIL)
                .phone(NEW_USER_PHONE)
                .firstName(NEW_USER_FIRST_NAME)
                .build();
    }

    public static Ad adGenerator() {
        Ad ad = new Ad();
        ad.setId(AD_ID_1);
        ad.setTitle(AD_TITLE_1);
        ad.setDescription(AD_DESCRIPTION_1);
        ad.setPrice(AD_PRICE_1);
        ad.setImageUrl(AD_IMAGE_1);
        ad.setFileSize(AD_FILE_SIZE_1);
        ad.setMediaType(AD_MEDIA_TYPE_1);
        ad.setAuthor(AD_AUTHOR_1);
        return ad;
    }

    public static AdDto adDtoGenerator() {
        return AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
    }

    public static List<Ad> listAdsGenerator() {
        Ad ad1 = new Ad();
        ad1.setId(AD_ID_1);
        ad1.setTitle(AD_TITLE_1);
        ad1.setDescription(AD_DESCRIPTION_1);
        ad1.setPrice(AD_PRICE_1);
        ad1.setImageUrl(AD_IMAGE_1);
        ad1.setFileSize(AD_FILE_SIZE_1);
        ad1.setMediaType(AD_MEDIA_TYPE_1);
        ad1.setAuthor(AD_AUTHOR_1);
        Ad ad2 = new Ad();
        ad2.setId(AD_ID_2);
        ad2.setTitle(AD_TITLE_2);
        ad2.setDescription(AD_DESCRIPTION_2);
        ad2.setPrice(AD_PRICE_2);
        ad2.setImageUrl(AD_IMAGE_2);
        ad2.setFileSize(AD_FILE_SIZE_2);
        ad2.setMediaType(AD_MEDIA_TYPE_2);
        ad2.setAuthor(AD_AUTHOR_2);
        return List.of(ad1, ad2);
    }

    public static List<AdDto> listAdsDtoGenerator() {
        AdDto adDto1 = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
        AdDto adDto2 = AdDto.builder()
                .author(AD_AUTHOR_ID_2)
                .image("/image/" + AD_IMAGE_2)
                .pk(AD_ID_2)
                .price(AD_PRICE_2)
                .title(AD_TITLE_2)
                .build();
        return List.of(adDto1, adDto2);
    }

    public static ExtendedAdDto extendedAdDtoGenerator() {
        return ExtendedAdDto.builder()
                .pk(AD_ID_1)
                .description(AD_DESCRIPTION_1)
                .image("/image/" + AD_IMAGE_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .email(AD_AUTHOR_EMAIL_1)
                .authorFirstName(AD_AUTHOR_FIRST_NAME_1)
                .authorLastName(AD_AUTHOR_LAST_NAME_1)
                .phone(AD_AUTHOR_PHONE_1)
                .build();
    }

    public static CreateOrUpdateAdDto createOrUpdateAdDtoGenerator() {
        return CreateOrUpdateAdDto.builder()
                .title(AD_NEW_TITLE_1)
                .price(AD_NEW_PRICE_1)
                .description(AD_NEW_DESCRIPTION_1)
                .build();
    }

    public static AdDto newAdDtoGenerator() {
        return AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .title(AD_NEW_TITLE_1)
                .price(AD_NEW_PRICE_1)
                .build();
    }


}
