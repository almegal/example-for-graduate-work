package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageUploadService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.AD_IMAGE_1;

@WebMvcTest(controllers = AdController.class)
public class AdControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdRepository adRepository;

    @MockBean
    private AdMapper adMapper;

    @MockBean
    private ImageUploadService imageUploadService;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityServiceImpl securityService;

    @MockBean
    private UserSecurityDetails userSecurityDetails;

    @SpyBean
    private AdServiceImpl adService;

    @InjectMocks
    private AdController adsController;

    private ObjectMapper objectMapper;
    private User user;
    private User userAdmin;
    private Ad ad1;
    private Ad newAd1;
    private Ad newAd2;
    private AdDto adDto1;
    private Ad ad2;
    private AdDto adDto2;
    private AdDto newAdDto1;
    private AdDto newAdDto2;
    private ExtendedAdDto extendedAdDto;
    private List<Ad> ads;
    private List<AdDto> adsDto;
    private CreateOrUpdateAdDto createOrUpdateAdDto1;
    private CreateOrUpdateAdDto createOrUpdateAdDto2;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        user = ConstantGeneratorFotTest.userGenerator();
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        ad1 = ConstantGeneratorFotTest.adGenerator1();
        newAd1 = ConstantGeneratorFotTest.newAdGenerator1();
        newAd2 = ConstantGeneratorFotTest.newAdGenerator2();
        adDto1 = ConstantGeneratorFotTest.adDtoGenerator1();
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        adDto2 = ConstantGeneratorFotTest.adDtoGenerator2();
        ads = ConstantGeneratorFotTest.listAdsGenerator();
        adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
        createOrUpdateAdDto1 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator1();
        createOrUpdateAdDto2 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator2();
        extendedAdDto = ConstantGeneratorFotTest.extendedAdDtoGenerator();
        newAdDto1 = ConstantGeneratorFotTest.newAdDtoGenerator();
        newAdDto2 = ConstantGeneratorFotTest.newAdDtoGenerator2();

    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetAds_Success() throws Exception {

        when(adRepository.findAll()).thenReturn(ads);
        when(adMapper.toDtos(anyList())).thenReturn(adsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(adsDto.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].author")
                        .value(adsDto.get(0).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].image")
                        .value(adsDto.get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].pk").value(adsDto.get(0).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].title")
                        .value(adsDto.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].price")
                        .value(adsDto.get(0).getPrice()));
  }

    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testAddAd_Success() throws Exception {

        Path path = Path.of("images/" + AD_IMAGE_1);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", AD_IMAGE_1, contentType, content);

        //when(image.getBytes()).thenReturn(new byte[]{});
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);
        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(adDto1);


        //when(adService.addAd(eq(createOrUpdateAdDto1), any(MultipartFile.class))).thenReturn(adDto1);

//        MockMultipartFile propertiesJson = new MockMultipartFile(
//                "properties",
//                "test.jpg",
//                "application/json",
//                objectMapper.writeValueAsBytes(createOrUpdateAdDto1));

        MockMultipartFile propertiesJson = new MockMultipartFile(
                "properties",
                "properties.json",
                "application/json",
                "{\"title\": \"Title 1\", \"price\": 5000, \"description\": \"Description 1\"}".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file("image", image.getBytes())
                        .file(propertiesJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetExtendedAd_Success() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adMapper.toExtendedDto(any(Ad.class))).thenReturn(extendedAdDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(extendedAdDto.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(extendedAdDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(extendedAdDto.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(extendedAdDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(extendedAdDto.getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(extendedAdDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFirstName")
                        .value(extendedAdDto.getAuthorFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorLastName")
                        .value(extendedAdDto.getAuthorLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(extendedAdDto.getPhone()));
    }

    @WithAnonymousUser
    @Test
    public void testGetExtendedAd_Unauthorized() throws Exception {

        //when(userSecurityDetails.getUsername()).thenReturn(null);
        when(userService.getUserByEmailFromDb(anyString()))
                .thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetExtendedAd_NotFound() throws Exception {

        when(adRepository.findById(1L)).thenThrow(new NotFoundException("Объявление не найдено"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //@PreAuthorize("@adServiceImpl.isAdCreatorOrAdmin(#id)")
    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_Success_1() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(true);
        doNothing().when(adRepository).deleteById(ad1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_Success_2() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(userAdmin.getEmail());
        when(userService.getUserByEmailFromDb(userAdmin.getEmail())).thenReturn(userAdmin);
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(true);
        doNothing().when(adRepository).deleteById(ad1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Disabled("Тест временно не работает")
    @WithAnonymousUser
    @Test
    public void testRemoveAd_Unauthorized() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_NotFound() throws Exception {

        when(adRepository.findById(1L)).thenThrow(new NotFoundException("Объявление не найдено"));
        doNothing().when(adRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_ForbiddenException_1() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        // Изменяем у объявления поле - пользователь
        ad1.getAuthor().setEmail("test@gmail.com");
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(false);
        doNothing().when(adRepository).deleteById(ad1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_ForbiddenException_2() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(userAdmin.getEmail());
        when(userService.getUserByEmailFromDb(userAdmin.getEmail())).thenReturn(userAdmin);
        // Изменяем у пользователя поле - роль
        userAdmin.setRole(Role.USER);
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(false);
        doNothing().when(adRepository).deleteById(ad1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Disabled("Тест временно не работает")
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testUpdateAds_Success() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adService.isAdCreatorOrAdmin(ad1.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(AD_NEW_TITLE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(AD_NEW_PRICE_1));
        // поле "description" из jsonPath извлечь нельзя, так как метод возвращает AdDto (а в нем нет такого поля)

    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetAdsByAuthenticatedUser_Success() throws Exception {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adRepository.findAllByUserId(user.getId())).thenReturn(ads);
        when(adMapper.toDtos(anyList())).thenReturn(adsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(adsDto.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].author")
                        .value(adsDto.get(0).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].image")
                        .value(adsDto.get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].pk").value(adsDto.get(0).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].title")
                        .value(adsDto.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].price")
                        .value(adsDto.get(0).getPrice()));
    }

    @WithAnonymousUser
    @Test
    public void testGetAdsByAuthenticatedUser_Unauthorized() throws Exception {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adRepository.findAllByUserId(user.getId())).thenThrow(new UnauthorizedException("Пользователь не авторизован"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }



}
