package ru.skypro.homework.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@WebMvcTest(controllers = AdsController.class)
public class AdsControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdRepository adRepository;

    @MockBean
    private AdMapper adMapper;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private AdServiceImpl adService;

    @InjectMocks
    private AdsController adsController;

    private ObjectMapper objectMapper;


    @WithMockUser(value = "spring")
    @Test
    public void testGetAll() throws Exception {

        List<Ad> ads = ConstantGeneratorFotTest.listAdsGenerator();
        List<AdDto> adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();

        when(adRepository.findAll()).thenReturn(ads);
        when(adMapper.toDtos(any())).thenReturn(adsDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.count").value(2))
//                .andExpect(jsonPath("$.results").value(adsDto));


        mockMvc.perform(get("/ads")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results[0].title").value(adsDto.get(0).getTitle()));
  }

}
