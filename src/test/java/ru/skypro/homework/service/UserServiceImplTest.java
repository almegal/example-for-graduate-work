package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.util.UploadImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityServiceImpl securityService;

    @Mock
    private UserSecurityDetails userSecurityDetails;

    @InjectMocks
    private UserServiceImpl userService;


    private User user;
    private User userAdmin;
    private User newUser;
    private NewPasswordDto newPasswordDto;
    private NewPasswordDto incorrectPasswordDto;
    private UserDto userDto;
    private UserDto newUserDto1;
    private UserDto newUserDto2;
    private UpdateUserDto updateUserDto;


    @BeforeEach
    void setUp() {
        user = ConstantGeneratorFotTest.userGenerator();
        newUser = ConstantGeneratorFotTest.newUserGenerator_3();
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        newPasswordDto = ConstantGeneratorFotTest.newPasswordDtoGenerator();
        incorrectPasswordDto = ConstantGeneratorFotTest.IncorrectPasswordDtoGenerator();
        userDto = ConstantGeneratorFotTest.userDtoGenerator();
        newUserDto1 = ConstantGeneratorFotTest.newUserDtoGenerator_1();
        newUserDto2 = ConstantGeneratorFotTest.newUserDtoGenerator_2();
        updateUserDto = ConstantGeneratorFotTest.updateUserDtoGenerator();
    }


    @Test
    void testUpdatePassword_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        doNothing().when(userMapper).updateUserPasswordFromDto(newPasswordDto, user);

        // Пароль, извлеченный из user, и текущий пароль, извлеченный из newPasswordDto, совпадают.
        // Пароль изменен
        boolean isUpdatePassword = userService.updatePassword(newPasswordDto);

        assertTrue(isUpdatePassword);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Disabled("Тест временно не работает")
    @Test
    void testUpdatePassword_Unsuccessful() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        //newPasswordDto.setCurrentPassword(USER_INCORRECT_PASSWORD);
        doNothing().when(userMapper).updateUserPasswordFromDto(incorrectPasswordDto, user);

        // Пароль, извлеченный из user, и текущий пароль, извлеченный из newPasswordDto, не совпадают.
        // Пароль изменен
        boolean isUpdatePassword = userService.updatePassword(incorrectPasswordDto);

        assertFalse(isUpdatePassword);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testUpdatePassword_UnauthorizedException() {

        //when(userSecurityDetails.getUsername()).thenReturn(null);
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        assertThrows(UnauthorizedException.class, () -> userService.updatePassword(newPasswordDto));
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetAuthenticatedUser_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto actual = userService.getAuthenticatedUser();

        assertEquals(userDto, actual);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetAuthenticatedUser_UnauthorizedException() {

        //when(userSecurityDetails.getUsername()).thenReturn(null);
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        assertThrows(UnauthorizedException.class, () -> userService.getAuthenticatedUser());
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testUpdateAuthenticatedUserInfo_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        doNothing().when(userMapper).updateUserFromUpdateUserDto(updateUserDto, user);
        when(userMapper.toDto(any(User.class))).thenReturn(newUserDto1);

        UserDto expected = UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(NEW_USER_FIRST_NAME)
                .lastName(NEW_USER_LAST_NAME)
                .phone(NEW_USER_PHONE)
                .image(USER_IMAGE)
                .build();

        UserDto actual = userService.updateAuthenticatedUserInfo(updateUserDto);

        assertEquals(expected, actual);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testUpdateAuthenticatedUserInfo_UnauthorizedException() {

        //when(userSecurityDetails.getUsername()).thenReturn(null);
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        assertThrows(UnauthorizedException.class, () -> userService.updateAuthenticatedUserInfo(updateUserDto));
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Disabled("Тест временно не работает")
    @Test
    void testUpdateAuthenticatedUserImage_Success() throws IOException {

        // Необходимо добавить в папку images файл с именем - USER_IMAGE
        Path path = Path.of("images" + NEW_USER_IMAGE);
        String name = NEW_USER_IMAGE.substring(0, NEW_USER_IMAGE.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, NEW_USER_IMAGE, contentType, content);

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
//        user.setImage(NEW_USER_IMAGE);
//        userDto.setImage(NEW_USER_IMAGE);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userMapper.toDto(any(User.class))).thenReturn(newUserDto2);

        UserDto expected = UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .phone(USER_PHONE)
                .image(NEW_USER_IMAGE)
                .build();

        // Мокируем вызов статического метода по загрузке картинки
        try (MockedStatic<UploadImage> utilities =  Mockito.mockStatic(UploadImage.class)) {
            // Задаем поведение мокированного метода
            utilities.when(() -> UploadImage.uploadImage(any(MultipartFile.class))).thenReturn("/test.jpg");

            UserDto actual = userService.updateAuthenticatedUserImage(image);

            assertNotNull(actual);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getImage(), actual.getImage());
            verify(securityService, times(1)).getAuthenticatedUserName();
            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(userRepository, times(1)).save(newUser);
        }
    }

    @Test
    void testUpdateAuthenticatedUserImage_UnauthorizedException() throws IOException {

        // Необходимо добавить в папку images файл с именем - USER_IMAGE
        Path path = Path.of("images" + NEW_USER_IMAGE);
        String name = NEW_USER_IMAGE.substring(0, NEW_USER_IMAGE.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, NEW_USER_IMAGE, contentType, content);

        //when(userSecurityDetails.getUsername()).thenReturn(null);
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        assertThrows(UnauthorizedException.class, () -> userService.updateAuthenticatedUserImage(image));
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Disabled("Тест временно не работает")
    @Test
    void testLoadByUserName_Success() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        UserDetails expected = new UserSecurityDetails(user);
        UserDetails actual = userService.loadByUserName(user.getEmail());

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmailFromDb_Success() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        User actual = userService.getUserByEmailFromDb(user.getEmail());

        assertEquals(user, actual);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

}
