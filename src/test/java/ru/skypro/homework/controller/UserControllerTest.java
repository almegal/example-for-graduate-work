package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.SecurityServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private SecurityServiceImpl securityService;


    private User user;
    private User userAdmin;
    private User newUser;
    private NewPasswordDto newPasswordDto;
    private NewPasswordDto incorrectPasswordDto;
    private UserDto userDto;
    private UserDto newUserDto1;
    private UserDto newUserDto2;
    private UpdateUserDto updateUserDto;
    private RegisterDto registerDto;


    @BeforeEach
    void setUp() {

        user = ConstantGeneratorFotTest.userGenerator();
        user = userRepository.save(user);
        // Первый пользователь

        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        userAdmin = userRepository.save(userAdmin);
        // Администратор
    }


    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_Success() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_PASSWORD);
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_InvalidData() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_PASSWORD);
        newPasswordDto.put("newPassword", USER_INVALID_PASSWORD);
        // Введенный пароль содержит 5 символов

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_UnauthorizedException() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_PASSWORD);
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Disabled("Тест временно не работает")
    @Test
    @WithMockUser(username = USER_ADMIN_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_ForbiddenException_1() throws Exception {

        // С другого логина изменяем пароль текущего пользователя
        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_PASSWORD);
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Disabled("Тест временно не работает")
    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_ForbiddenException_2() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_INCORRECT_PASSWORD);
        // Введен неправильный текущий пароль
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAuthenticatedUser_Success() throws Exception {

        UserDto expected = UserDto.builder()
                .id(1L)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .phone(USER_PHONE)
                .image(USER_IMAGE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expected.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(expected.getRole().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(expected.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(expected.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(expected.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(expected.getImage()));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAuthenticatedUser_UnauthorizedException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserInfo_Success() throws Exception {

        JSONObject updateUserDto = new JSONObject();
        updateUserDto.put("fistName", NEW_USER_FIRST_NAME);
        updateUserDto.put("lastName", NEW_USER_LAST_NAME);
        updateUserDto.put("phone", NEW_USER_PHONE);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(updateUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(NEW_USER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(NEW_USER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(NEW_USER_PHONE));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserInfo_UnauthorizedException() throws Exception {

        JSONObject updateUserDto = new JSONObject();
        updateUserDto.put("fistName", NEW_USER_FIRST_NAME);
        updateUserDto.put("lastName", NEW_USER_LAST_NAME);
        updateUserDto.put("phone", NEW_USER_PHONE);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(updateUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserImage_Success() throws Exception {

        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", NEW_USER_IMAGE, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/users/me/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserImage_UnauthorizedException() throws Exception {

        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", NEW_USER_IMAGE, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/users/me/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    //@WithMockUser(username = USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDownloadImageForUser_Success() throws Exception {

        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", NEW_USER_IMAGE, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        user.setImage(NEW_USER_IMAGE);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/{imagePath}", NEW_USER_IMAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE));
        // IMAGE_JPEG_VALUE или IMAGE_JPEG
    }




}
