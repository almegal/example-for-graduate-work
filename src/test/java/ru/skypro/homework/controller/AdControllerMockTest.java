//package ru.skypro.homework.controllerTest;
//
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import ru.skypro.homework.config.WebSecurityConfig;
//import ru.skypro.homework.controller.AdController;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@Import(WebSecurityConfig.class)
//@WebMvcTest(controllers = AdController.class)
//public class AdControllerMockTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AdRepository adRepository;
//
//    @MockBean
//    private AdMapper adMapper;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @SpyBean
//    private AdServiceImpl adService;
//
//    @InjectMocks
//    private AdsController adsController;
//
//    private ObjectMapper objectMapper;
//
//
//    @WithMockUser(value = "spring")
//    @Test
//    public void testGetAll() throws Exception {
//
//        List<Ad> ads = ConstantGeneratorFotTest.listAdsGenerator();
//        List<AdDto> adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
//
//        when(adRepository.findAll()).thenReturn(ads);
//        when(adMapper.toDtos(any())).thenReturn(adsDto);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/ads")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.count").value(2))
//                .andExpect(jsonPath("$.results").value(adsDto));


//        mockMvc.perform(get("/ads")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.count").value(2))
//                .andExpect(jsonPath("$.results[0].title").value(adsDto.get(0).getTitle()));
//  }

//}
