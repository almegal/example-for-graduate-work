package ru.skypro.homework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.util.UploadImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;


@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdMapper adMapper;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityServiceImpl securityService;

    @InjectMocks
    private AdServiceImpl adService;

    private ObjectMapper objectMapper;
    private User user;
    private User userAdmin;
    private Ad ad1;
    private AdDto adDto1;
    private Ad ad2;
    private AdDto adDto2;
    private AdDto newAdDto1;
    private ExtendedAdDto extendedAdDto;
    private List<Ad> ads;
    private List<AdDto> adsDto;
    private CreateOrUpdateAdDto createOrUpdateAdDto1;
    private CreateOrUpdateAdDto createOrUpdateAdDto2;


    @BeforeEach
    void setUp() {
        user = ConstantGeneratorFotTest.userGenerator();
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        ad1 = ConstantGeneratorFotTest.adGenerator1();
        adDto1 = ConstantGeneratorFotTest.adDtoGenerator1();
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        adDto2 = ConstantGeneratorFotTest.adDtoGenerator2();
        ads = ConstantGeneratorFotTest.listAdsGenerator();
        adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
        createOrUpdateAdDto1 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator1();
        createOrUpdateAdDto2 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator2();
        extendedAdDto = ConstantGeneratorFotTest.extendedAdDtoGenerator();
        newAdDto1 = ConstantGeneratorFotTest.newAdDtoGenerator();
    }

    @Test
    void testGetAds_Success() {

        when(adRepository.findAll()).thenReturn(ads);
        when(adMapper.toDtos(any())).thenReturn(adsDto);

        AdsDto actual = adService.getAds();

        assertNotNull(actual);
        assertEquals(2, actual.getCount());
        assertEquals(adsDto.size(), actual.getResults().size());
        assertThat(adsDto).isEqualTo(actual.getResults());
        verify(adRepository, times(1)).findAll();
    }

    @Test
    void testAddAd_Success() throws IOException {

//        mockStatic(UploadImage.class);
//        doNothing().when(UploadImage.uploadImage(any()));



        Path path = Path.of("images/" + AD_IMAGE_1);
        String name = AD_IMAGE_1.substring(0, AD_IMAGE_1.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, AD_IMAGE_1, contentType, content);
        // name - имя файла без "." и расширения; AD_IMAGE_1 - имя файла с "." и расширением;

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);
        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(adDto1);
        //when(image.getBytes()).thenReturn(new byte[]{});
        //when(adService.addAd(eq(createOrUpdateAdDto), any(MultipartFile.class))).thenReturn(adDto1);

        AdDto expected = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();

        try (MockedStatic<UploadImage> utilities =  Mockito.mockStatic(UploadImage.class)) {
            //добавляем правило
            utilities.when(() -> UploadImage.uploadImage(any())).thenReturn("/test.jpg");
            //проверяем, что правило работает
        }

        AdDto actual = adService.addAd(createOrUpdateAdDto1, image);

        assertNotNull(actual);
        assertEquals(expected.getPk(), actual.getPk());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userService, times(1)).getUserByEmailFromDb(user.getEmail());
        verify(adRepository, times(1)).save(any(Ad.class));
    }

//    @Test
//    void testAddAd_ThrowsUnauthorizedException() {
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(false);
//        assertThrows(UnauthorizedException.class, () -> adService.addAd(
//                createOrUpdateAdDto,
//                fileImage,
//                authentication));
//    }

    @Test
    void testAddAdUser_ThrowsNotFoundException() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenThrow(new NotFoundException("Пользователь не найден"));
        assertThrows(NotFoundException.class, () -> adService.addAd(createOrUpdateAdDto1, null));
    }

    @Test
    void testGetExtendedAd_Success() {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adMapper.toExtendedDto(any(Ad.class))).thenReturn(extendedAdDto);

        ExtendedAdDto actual = adService.getExtendedAd(ad1.getId());

        assertNotNull(actual);
        assertEquals(extendedAdDto.getPk(), actual.getPk());
        assertEquals(extendedAdDto.getTitle(), actual.getTitle());
        assertEquals(extendedAdDto.getPrice(), actual.getPrice());
        assertEquals(extendedAdDto.getDescription(), actual.getDescription());
        assertEquals(extendedAdDto.getImage(), actual.getImage());
        assertEquals(extendedAdDto.getEmail(), actual.getEmail());
        assertEquals(extendedAdDto.getAuthorFirstName(), actual.getAuthorFirstName());
        assertEquals(extendedAdDto.getAuthorLastName(), actual.getAuthorLastName());
        assertEquals(extendedAdDto.getPhone(), actual.getPhone());
        verify(adRepository, times(1)).findById(ad1.getId());
    }

    @Test
    void testGetExtendedAd_ThrowsNotFoundException() {

        when(adRepository.findById(anyLong())).thenThrow(new NotFoundException("Объявление не найдено"));
        assertThrows(NotFoundException.class, () -> adService.getExtendedAd(ad1.getId()));
    }

    @Test
    void testRemoveAd_Success() throws IOException {

//        mockStatic(Files.class);
//        doNothing().when(Files.deleteIfExists(any()));
//        doThrow(new RuntimeException()).when(Files.class);
//        Files.deleteIfExists(any());
//        verifyStatic(times(1));
//        Files.deleteIfExists(any());

        when(adRepository.findById(ad2.getId())).thenReturn(Optional.of(ad2));
        doNothing().when(adRepository).deleteById(ad2.getId());

        adService.removeAd(ad2.getId());

        assertFalse(Files.exists(Path.of("images/" + ad2.getImageUrl())));
        verify(adRepository, times(1)).findById(ad2.getId());
        verify(adRepository, times(1)).deleteById(ad2.getId());
    }

    @Test
    @Disabled("Тест временно отключен")
    void testRemoveAd_ThrowsUnauthorizedException() {


    }

    @Test
    void testRemoveAd_ThrowsNotFoundException() {

        when(adRepository.findById(anyLong())).thenThrow(new NotFoundException("Объявление не найдено"));
        assertThrows(NotFoundException.class, () -> adService.removeAd(180L));
    }

    @Test
    void testUpdateAd_Success() {
        // необходимо вызвать метод isAdCreatorOrAdmin()
        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto1);

        AdDto expected = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_NEW_PRICE_1)
                .title(AD_NEW_TITLE_1)
                .build();

        AdDto actual = adService.updateAd(ad1.getId(), createOrUpdateAdDto2);

        assertNotNull(actual);
        assertEquals(expected.getPk(), actual.getPk());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        verify(adRepository, times(1)).findById(ad1.getId());
        verify(adRepository, times(1)).save(ad1);
    }

    @Test
    void testUpdateAd_ThrowsNotFoundException() {

        when(adRepository.findById(anyLong())).thenThrow(new NotFoundException("Объявление не найдено"));
        assertThrows(NotFoundException.class, () -> adService.updateAd(134L, createOrUpdateAdDto2));
    }

    @Test
    void testUpdateImageAd_Success() throws Exception {

        Path path = Path.of("images/" + AD_IMAGE_1);
        String name = AD_IMAGE_1.substring(0, AD_IMAGE_1.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, AD_IMAGE_1, contentType, content);

        // необходимо вызвать метод isAdCreatorOrAdmin()
        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(adDto1);

        AdDto expected = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image("/image/" + AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_PRICE_1)
                .title(AD_TITLE_1)
                .build();

        AdDto actual = adService.updateImageAd(ad1.getId(), image);

        assertNotNull(actual);
        assertEquals(expected.getPk(), actual.getPk());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        verify(adRepository, times(1)).findById(ad1.getId());
        verify(adRepository, times(1)).save(ad1);
    }

    @Test
    void testUpdateImageAd_ThrowsNotFoundException() {

        when(adRepository.findById(anyLong())).thenThrow(new NotFoundException("Объявление не найдено"));
        assertThrows(NotFoundException.class, () -> adService.updateImageAd(134L, null));
    }

    @Test
    void testFindAdById_Success() {

        doReturn(Optional.of(ad1)).when(adRepository).findById(anyLong());
        // как правильно: anyLong() или Long.class ?

        Ad actual = adService.findAdById(ad1.getId());

        assertAll("Сложный сценарий сравнения объявления",
                () -> assertEquals(ad1.getId(), actual.getId()),
                () -> assertEquals(ad1.getDescription(), actual.getDescription()),
                () -> assertEquals(ad1.getPrice(), actual.getPrice()),
                () -> assertEquals(ad1.getTitle(), actual.getTitle()),
                () -> assertEquals(ad1.getImageUrl(), actual.getImageUrl()),
                () -> assertEquals(ad1.getAuthor(), actual.getAuthor())
        );
    }

    @Test
    void testFindAdById_ThrowsNotFoundException() {

        doThrow(new NotFoundException("Объявление не найдено")).when(adRepository).findById(153L);
        assertThrows(NotFoundException.class, () -> adService.findAdById(153L));

    }

    @Test
    void testIsAdCreatorOrAdmin1_Success() {

        doReturn(Optional.of(ad1)).when(adRepository).findById(anyLong());
        doReturn(user.getEmail()).when(securityService).getAuthenticatedUserName();
        doReturn(user).when(userService).getUserByEmailFromDb(anyString());
        ad1.getAuthor().setEmail("test@gmail.com");
        //doReturn(user.getEmail()).when(ad1).getAuthor().getEmail();

        boolean actualIsAuthorOfAd = adService.isAdCreatorOrAdmin(user.getId());
        assertFalse(actualIsAuthorOfAd);

        verify(adRepository, times(1)).findById(anyLong());
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userService, times(1)).getUserByEmailFromDb(user.getEmail());
    }

    @Test
    void testIsAdCreatorOrAdmin2_Success() {

        doReturn(Optional.of(ad1)).when(adRepository).findById(anyLong());
        doReturn(userAdmin.getEmail()).when(securityService).getAuthenticatedUserName();
        doReturn(userAdmin).when(userService).getUserByEmailFromDb(anyString());
        userAdmin.setRole(Role.USER);
//        doReturn(Role.ADMIN).when(userAdmin).getRole();

        boolean actualIsAdmin = adService.isAdCreatorOrAdmin(userAdmin.getId());
        assertFalse(actualIsAdmin);

        verify(adRepository, times(1)).findById(anyLong());
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userService, times(1)).getUserByEmailFromDb(userAdmin.getEmail());
    }

}
