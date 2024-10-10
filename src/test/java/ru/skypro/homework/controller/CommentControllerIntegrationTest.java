package ru.skypro.homework.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserSecurityDetails;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @Mock
    private UserSecurityDetails userSecurityDetails;

    private Ad ad;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // Сначала сохраняем пользователя
        user = ConstantGeneratorFotTest.userGenerator(); // Используем email "testuser@example.com"
        user = userRepository.save(user); // Сохраняем и получаем пользователя с ID

        // Затем сохраняем объявление, убедившись, что автор установлен
        ad = ConstantGeneratorFotTest.adGenerator1();
        ad.setAuthor(user);
        ad = adRepository.save(ad); // Сохраняем и получаем объявление с ID

        // Наконец, сохраняем комментарий
        comment = ConstantGeneratorFotTest.commentGenerator(ad, user);
        commentRepository.save(comment);

        when(userSecurityDetails.getUsername()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userSecurityDetails,
                        null,
                        userSecurityDetails.getAuthorities())
        );
    }

    @Test
    @Order(1)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddComment() throws Exception {

        CreateOrUpdateCommentDto createCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Test Comment")
                .build();

        mockMvc.perform(post("/ads/{adId}/comments", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test Comment"));
    }

    @Test
    @Order(2)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateComment() throws Exception {
        CreateOrUpdateCommentDto updateCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Updated Comment")
                .build();

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", ad.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Comment"));
    }

    @Test
    @Order(3)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetComments() throws Exception {
        mockMvc.perform(get("/ads/{adId}/comments", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }

    @Test
    @Order(4)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", ad.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
