package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AdService adService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

//    @Mock
//    private UserSecurityDetails userSecurityDetails;

    private ObjectMapper objectMapper;
    private User user;
    private User userAdmin;
    private Ad ad1;
    private Ad ad2;
    private CreateOrUpdateAdDto createOrUpdateAdDto1;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        user = ConstantGeneratorFotTest.userGenerator();
        user = userRepository.save(user);
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        // Модель для создания объявления
        createOrUpdateAdDto1 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator1();

//        when(userSecurityDetails.getUsername()).thenReturn(user.getEmail());
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(
//                        userSecurityDetails,
//                        null,
//                        userSecurityDetails.getAuthorities())
//        );
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddAd_Success() throws Exception {

        // Необходимо создать в папке images реальный файл: 55.jpg
        Path path = Path.of("images/" + AD_IMAGE_1);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", AD_IMAGE_1, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

//        MockMultipartFile image = new MockMultipartFile(
//                "image",
//                "test.jpg",
//                "image/jpeg",
//                "test image".getBytes());
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "test.jpg",
                "application/json",
                objectMapper.writeValueAsBytes(createOrUpdateAdDto1));

        AdDto expected = AdDto.builder()
                .author(1L)
                .pk(1L)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();

        mockMvc.perform(multipart("/ads")
                        .file(properties)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(expected.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(expected.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(expected.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expected.getTitle()));
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddAd_UnauthorizedException() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "test.jpg",
                "application/json",
                objectMapper.writeValueAsBytes(createOrUpdateAdDto1));

        mockMvc.perform(multipart("/ads")
                        .file(properties)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddAd_InvalidData() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_INVALID_TITLE_1);
        createOrUpdateAdDto.put("price", AD_INVALID_PRICE_1);
        createOrUpdateAdDto.put("description", AD_INVALID_DESCRIPTION_1);

        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "test.jpg",
                "application/json",
                objectMapper.writeValueAsBytes(createOrUpdateAdDto));

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        mockMvc.perform(multipart("/ads")
                        .file(properties)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetExtendedAd_Success() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        ExtendedAdDto expected = ExtendedAdDto.builder()
                .pk(1L)
                .description(AD_DESCRIPTION_1)
                .image("/image/" + ad1.getImageUrl())
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .email(AD_AUTHOR_EMAIL_1)
                .authorFirstName(AD_AUTHOR_FIRST_NAME_1)
                .authorLastName(AD_AUTHOR_LAST_NAME_1)
                .phone(AD_AUTHOR_PHONE_1)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(expected.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expected.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(expected.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expected.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(expected.getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expected.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFirstName")
                        .value(expected.getAuthorFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorLastName")
                        .value(expected.getAuthorLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(expected.getPhone()));
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetExtendedAd_UnauthorizedException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // Выброс исключения: объявление не найдено
    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetExtendedAd_NotFoundException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_Success_1() throws Exception {

        // Можно создать в папке images реальный файл: 60.jpg
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2.setAuthor(user);
        ad2 = adRepository.save(ad2);

        // Удаление объявления пользователя осуществляем с логина этого же пользователя
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_ADMIN_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_Success_2() throws Exception {

        // Можно создать в папке images реальный файл: 60.jpg
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2.setAuthor(user);
        ad2 = adRepository.save(ad2);

        // Сохраняем в базу данных администратора
        userAdmin = userRepository.save(userAdmin);

        // Удаление объявления пользователя осуществляем с логина администратора
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_Unauthorized() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_NotFoundException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_ForbiddenException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        user.setEmail(NEW_USER_EMAIL);
        // Установили пользователю другой логин
        user.setId(2L);
        // Установили пользователю другой id
        userRepository.save(user);
        // Сохранили пользователя в базу данных
        ad1.setAuthor(user);
        // Установили в объявление измененного автора
        ad1 = adRepository.save(ad1);

        // Удаляем объявление с логина первоначального пользователя, который это объявление не создавал
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_Success_1() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_NEW_DESCRIPTION_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(AD_NEW_TITLE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(AD_NEW_PRICE_1));
                // поле "description" из jsonPath извлечь нельзя, так как метод возвращает AdDto (а в нем нет такого поля)
    }

    @Test
    @WithMockUser(username = USER_ADMIN_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_Success_2() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);
        // Сохраняем в базу данных администратора
        userRepository.save(userAdmin);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_NEW_DESCRIPTION_1);

        // С логина администратора будем изменять объявление
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(AD_NEW_TITLE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(AD_NEW_PRICE_1));
        // поле "description" из jsonPath извлечь нельзя, так как метод возвращает AdDto (а в нем нет такого поля)
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_UnauthorizedException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_NEW_DESCRIPTION_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_NotFoundException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_NEW_DESCRIPTION_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/2")
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_ForbiddenException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        // Изменили у пользователя логин
        user.setEmail(NEW_USER_EMAIL);
        // Изменили у пользователя id
        user.setId(null);
        // Сохранили измененного пользователя в базу данных
        userRepository.save(user);
        // Изменили в объявлении поле: user
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_NEW_DESCRIPTION_1);

        // Изменяем информацию об объявлении с логина первоначального пользователя, который данное объявление не создавал
        // В результате выбрасывается исключение: отсутствуют права на изменение объявления
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/1")
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAd_InvalidData() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        JSONObject createOrUpdateAdDTO = new JSONObject();
        createOrUpdateAdDTO.put("title", AD_INVALID_TITLE_1);
        createOrUpdateAdDTO.put("price", AD_INVALID_PRICE_1);
        createOrUpdateAdDTO.put("description", AD_INVALID_DESCRIPTION_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/1")
                        .content(createOrUpdateAdDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateImageAd_Success_1() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", 1L)
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

    @Test
    @WithMockUser(username = USER_ADMIN_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateImageAd_Success_2() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        userRepository.save(userAdmin);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", 1L)
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

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateImageAd_UnauthorizedException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", 1L)
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

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateImageAd_NotFoundException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", 3L)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateImageAd_ForbiddenException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        user.setEmail(NEW_USER_EMAIL);
        user.setId(null);
        userRepository.save(user);
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);
        // Теперь у объявления поле author изменено, а изменять картинку мы будем с логина первоначального пользователя

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", 1L)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAdsByAuthenticatedUser_Success() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2.setAuthor(user);
        ad2 = adRepository.save(ad2);

        AdDto adDto1 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_1)
                .pk(1L)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
        AdDto adDto2 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_2)
                .pk(2L)
                .price(AD_PRICE_2)
                .title(AD_TITLE_2)
                .build();
        List<AdDto> expected = List.of(adDto1, adDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(expected.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].author")
                .value(expected.get(0).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].image")
                        .value(expected.get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].pk")
                        .value(expected.get(0).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].price")
                        .value(expected.get(0).getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].title")
                        .value(expected.get(0).getTitle()));
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAdsByAuthenticatedUser_UnauthorizedException() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2.setAuthor(user);
        ad2 = adRepository.save(ad2);

        AdDto adDto1 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_1)
                .pk(1L)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
        AdDto adDto2 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_2)
                .pk(2L)
                .price(AD_PRICE_2)
                .title(AD_TITLE_2)
                .build();
        List<AdDto> expected = List.of(adDto1, adDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAds_Success() throws Exception {

        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        ad1 = adRepository.save(ad1);
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        ad2.setAuthor(user);
        ad2 = adRepository.save(ad2);

        AdDto adDto1 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_1)
                .pk(1L)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();
        AdDto adDto2 = AdDto.builder()
                .author(1L)
                .image("/image/" + AD_IMAGE_2)
                .pk(2L)
                .price(AD_PRICE_2)
                .title(AD_TITLE_2)
                .build();
        List<AdDto> expected = List.of(adDto1, adDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(expected.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].author")
                        .value(expected.get(1).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].image")
                        .value(expected.get(1).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].pk")
                        .value(expected.get(1).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].price")
                        .value(expected.get(1).getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].title")
                        .value(expected.get(1).getTitle()));
    }

}
