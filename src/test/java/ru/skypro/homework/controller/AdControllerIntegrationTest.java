package ru.skypro.homework.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserSecurityDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdService adService;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserSecurityDetails userSecurityDetails;

    private User user;
    private Ad ad1;
    private AdDto adDto1;
    private List<Ad> ads;
    private List<AdDto> adsDto;
    private CreateOrUpdateAdDto createOrUpdateAdDto;
    private ExtendedAdDto extendedAdDto;
    private AdDto newAdDto1;

    @BeforeEach
    void setUp() {

        // Сначала создаем пользователя
        user = ConstantGeneratorFotTest.userGenerator();
        // Сохраняем пользователя в базу данных и получаем пользователя с ID
        user = userRepository.save(user);

        // Затем создаем объявление, убедившись, что автор установлен
        ad1 = ConstantGeneratorFotTest.adGenerator1();
        ad1.setAuthor(user);
        // Сохраняем объявление в базу данных и получаем объявление с ID
        ad1 = adRepository.save(ad1);

        when(userSecurityDetails.getUsername()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userSecurityDetails,
                        null,
                        userSecurityDetails.getAuthorities())
        );

//        adDto1 = ConstantGeneratorFotTest.adDtoGenerator();
//        ads = ConstantGeneratorFotTest.listAdsGenerator();
//        adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
//        createOrUpdateAdDto = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator();
//        extendedAdDto = ConstantGeneratorFotTest.extendedAdDtoGenerator();
//        newAdDto1 = ConstantGeneratorFotTest.newAdDtoGenerator();
    }

    @Disabled("Тест временно отключен")
    @Test
    @Order(1)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddAd_Success() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "test.jpg",
                "application/json",
                objectMapper.writeValueAsBytes(createOrUpdateAdDto));

        CreateOrUpdateCommentDto createCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Test Comment")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                .file(properties)
                .file(image))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ad1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(ad1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(ad1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(ad1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(ad1.getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(ad1.getAuthor()));
    }

//    @Test
//    @Order(3)
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    public void testGetAds_Success() throws Exception {
//
//        mockMvc.perform(get("/ads")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.count").value(1))
//                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
//    }

    
    @Disabled("Тест временно отключен")
    @Test
    @Order(4)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRemoveAd_Success() throws Exception {

        mockMvc.perform(delete("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

//        mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/ads/{id}" + ad1.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}
