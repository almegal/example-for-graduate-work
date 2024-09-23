package ru.skypro.homework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@ExtendWith(MockitoExtension.class)
public class AdServiceImplTest {

    @Mock
    private AdMapper adMapper;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
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
    void testGetAds() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(adRepository.findAll()).thenReturn(ads);
        when(adMapper.toDtos(any())).thenReturn(adsDto);

        AdsDto actual = adService.getAds(authentication);

        assertNotNull(actual);
        assertEquals(2, actual.getCount());
        assertEquals(adsDto.size(), actual.getResults().size());
        assertThat(adsDto).isEqualTo(actual.getResults());
        verify(adRepository, times(1)).findAll();
    }

    @Test
    void testGetAdsUnauthorizedException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> adService.getAds(authentication));
    }

//    @Test
//    void testAddAd() throws IOException {
//
//        Path path = Paths.get(AD_IMAGE_1);
//        String name = path.getFileName().toString();
//        String originalFileName = name;
//        String contentType = Files.probeContentType(path);
//        byte[] content = Files.readAllBytes(path);
//        MockMultipartFile image = new MockMultipartFile(name, originalFileName, contentType, content);
//        MockMultipartFile properties = new MockMultipartFile(
//                "properties",
//                "",
//                "application/json",
//                objectMapper.writeValueAsBytes(createOrUpdateAdDto));
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.ofNullable(user));
//        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
//        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
//        when(adMapper.toDto(any())).thenReturn(adDto1);
//        when(image.getBytes()).thenReturn(new byte[]{});
//        when(adService.addAd(eq(createOrUpdateAdDto), any(MultipartFile.class), any(Authentication.class)))
//                .thenReturn(adDto1);
//
//        AdDto expected = AdDto.builder()
//                .author(AD_AUTHOR_ID_1)
//                .image(AD_IMAGE_1)
//                .pk(AD_ID_1)
//                .price(AD_NEW_PRICE_1)
//                .title(AD_NEW_TITLE_1)
//                .build();
//
//        AdDto actual = adService.addAd(createOrUpdateAdDto, image, authentication);
//        byte[] expectedImage = Files.readAllBytes(Path.of(
//                "/Users/alex/Desktop/Skypro_проекты/example-for-graduate-work/photos_2/" + AD_ID_1 + ".jpg"));
//        byte[] actualImage =
//
//
//                assertNotNull(actual);
//        assertEquals(expected.getPk(), actual.getPk());
//        assertEquals(expected.getTitle(), actual.getTitle());
//        assertEquals(expected.getPrice(), actual.getPrice());
//        assertEquals(expected.getImage(), actual.getImage());
//        assertEquals(expected.getAuthor(), actual.getAuthor());
//        verify(userRepository, times(1)).findByEmail(authentication.getName());
//        verify(adRepository, times(1)).save(any(Ad.class));
//          //или
//        verify(adRepository, times(1)).save(ad1);
//    }
//
//    @Test
//    void testAddAdUnauthorizedException() {
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(false);
//        assertThrows(UnauthorizedException.class, () -> adService.addAd(
//                createOrUpdateAdDto,
//                fileImage,
//                authentication));
//    }
//
//    @Test
//    void testAddAdUserNotFoundException() {
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(true);
//
//        assertThrows(NotFoundException.class, () -> adService.addAd(
//                createOrUpdateAdDto,
//                fileImage,
//                authentication));
//    }

    @Test
    void testGetExtendedAd() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(adRepository.findById(any(Long.class))).thenReturn(Optional.of(ad1));
        when(adMapper.toExtendedDto(any(Ad.class))).thenReturn(extendedAdDto);

        ExtendedAdDto actual = adService.getExtendedAd(ad1.getId(), authentication);

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
    void testGetExtendedAdUnauthorizedException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> adService.getExtendedAd(ad1.getId(), authentication));
    }

    @Test
    void testGetExtendedAdNotFoundException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        assertThrows(NotFoundException.class, () -> adService.getExtendedAd(ad1.getId(), authentication));
    }

    @Test
    void testRemoveAd() throws IOException {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(adRepository.findById(any(Long.class))).thenReturn(Optional.of(ad1));
        doNothing().when(adRepository).deleteById(any(Long.class));

        adService.removeAd(ad1.getId(), authentication);

        verify(adRepository, times(1)).findById(ad1.getId());
        verify(adRepository, times(1)).deleteById(ad1.getId());
    }

    @Test
    void testRemoveAdUnauthorizedException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> adService.removeAd(ad1.getId(), authentication));
    }

    @Test
    void testRemoveAdNotFoundException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        assertThrows(NotFoundException.class, () -> adService.removeAd(ad1.getId(), authentication));
    }

    @Test
    void testUpdateAd() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(adRepository.findById(any(Long.class))).thenReturn(Optional.of(ad1));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto1);

        AdDto expected = AdDto.builder()
                .author(AD_AUTHOR_ID_1)
                .image(AD_IMAGE_1)
                .pk(AD_ID_1)
                .price(AD_NEW_PRICE_1)
                .title(AD_NEW_TITLE_1)
                .build();

        AdDto actual = adService.updateAd(ad1.getId(), createOrUpdateAdDto, authentication);

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
    void testUpdateAdUnauthorizedException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> adService.updateAd(ad1.getId(), createOrUpdateAdDto, authentication));
    }

    @Test
    void testUpdateAdNotFoundException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        assertThrows(NotFoundException.class, () -> adService.updateAd(ad1.getId(), createOrUpdateAdDto, authentication));
    }



//    @Test
//    void testUpdateImageAd() {
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(adRepository.findById(any(Long.class))).thenReturn(Optional.of(ad1));
//
//        byte[] image = adService.updateImageAd(ad1.getId(), file, authentication);
//
//    }

    
}
