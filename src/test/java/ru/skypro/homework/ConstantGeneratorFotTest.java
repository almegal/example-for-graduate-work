package ru.skypro.homework;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.dto.Role;

import java.util.Random;

public class ConstantGeneratorFotTest {
    public static final Long USER_ID = new Random().nextLong();
    public static final String USER_EMAIL = "somecool@mail.com";
    public static final String USER_FIRST_NAME = "Jonh";
    public static final String USER_LAST_NAME = "Show";
    public static final String USER_PHONE = "9112223344";
    public static final Role USER_ROLE = Role.USER;
    public static final String USER_IMAGE = "http://example.com/avatar.jpg";
    public static final String USER_PASSWORD = "supersecretpassword";
    public static final String NEW_USER_EMAIL = "dontcool@mail.com";
    public static final String NEW_USER_FIRST_NAME = "Farhod";
    public static final String NEW_USER_LAST_NAME = "Blackwood";
    public static final String NEW_USER_PHONE = "9112443344";
    public static final Role NEW_USER_ROLE = Role.ADMIN;
    public static final String NEW_USER_IMAGE = "http://example.com/new_avatar.jpg";
    public static final String NEW_USER_PASSWORD = "notsecretpassword";

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
        ad.setId(1L);
        ad.setDescription("Test Ad Description");
        ad.setPrice(100);
        ad.setTitle("Test Ad Title");
        ad.setImageUrl("http://example.com/ad_image.jpg");
        ad.setAuthor(userGenerator());
        return ad;
    }

    public static Comment commentGenerator(Ad ad, User user) {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAd(ad);
        comment.setAuthor(user);
        return comment;
    }
}