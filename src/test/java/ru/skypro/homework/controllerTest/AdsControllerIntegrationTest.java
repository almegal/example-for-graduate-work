package ru.skypro.homework.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private AdRepository adRepository;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @SpyBean
//    private AdServiceImpl adService;

//    @InjectMocks
//    private AdsController adsController;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdServiceImpl adService;

    private ObjectMapper objectMapper;
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

        user = ConstantGeneratorFotTest.userGenerator();
        ad1 = ConstantGeneratorFotTest.adGenerator();
        adDto1 = ConstantGeneratorFotTest.adDtoGenerator();
        ads = ConstantGeneratorFotTest.listAdsGenerator();
        adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
        createOrUpdateAdDto = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator();
        extendedAdDto = ConstantGeneratorFotTest.extendedAdDtoGenerator();
        newAdDto1 = ConstantGeneratorFotTest.newAdDtoGenerator();
    }

    @Test
    @Order(1)
    //@WithMockUser(username = "testuser@example.com")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddAd() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes());
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(createOrUpdateAdDto));

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.ofNullable(user));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                .file(properties)
                .file(image))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ad1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(ad1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(ad1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(ad1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(ad1.getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileSize").value(ad1.getFileSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mediaType").value(ad1.getMediaType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(ad1.getAuthor()));
    }

}
