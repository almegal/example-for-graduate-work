package ru.skypro.homework.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.CommentRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CommentsControllerIntegrationTest {

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

    private Ad ad;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // Сначала сохраняем пользователя
        user = ConstantGeneratorFotTest.userGenerator(); // Используем email "testuser@example.com"
        user = userRepository.save(user); // Сохраняем и получаем пользователя с ID

        // Затем сохраняем объявление, убедившись, что автор установлен
        ad = ConstantGeneratorFotTest.adGenerator();
        ad.setAuthor(user);
        ad = adRepository.save(ad); // Сохраняем и получаем объявление с ID

        // Наконец, сохраняем комментарий
        comment = ConstantGeneratorFotTest.commentGenerator(ad, user);
        commentRepository.save(comment);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "testuser@example.com")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAddComment() throws Exception {
        CreateOrUpdateCommentDto createCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Test Comment")
                .build();

        mockMvc.perform(post("/ads/{adId}/comments", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Test Comment"));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "testuser@example.com")
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
    @WithMockUser(username = "testuser@example.com")
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
    @WithMockUser(username = "testuser@example.com")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", ad.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}