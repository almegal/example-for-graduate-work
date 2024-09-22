package ru.skypro.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

@ExtendWith(MockitoExtension.class)
class CommentsServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    private Comment comment;
    private CommentDto commentDto;
    private CreateOrUpdateCommentDto createOrUpdateCommentDto;
    private Ad ad;
    private ru.skypro.homework.entity.User user;

    @BeforeEach
    void setUp() {
        ad = ConstantGeneratorFotTest.adGenerator();
        user = ConstantGeneratorFotTest.userGenerator();
        comment = ConstantGeneratorFotTest.commentGenerator(ad, user);

        commentDto = new CommentDto();
        commentDto.setPk(1L);
        commentDto.setText("Test Comment");
        commentDto.setAuthor(ConstantGeneratorFotTest.USER_ID);

        createOrUpdateCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Updated Comment")
                .build();
    }

    @Test
    void getCommentsByAdId() {
        when(commentRepository.findByAdId(1L)).thenReturn(List.of(comment));
        when(commentMapper.toDtos(any())).thenReturn(List.of(commentDto));

        List<CommentsDto> commentsDtos = commentsService.getCommentsByAdId(1L);

        assertNotNull(commentsDtos);
        assertEquals(1, commentsDtos.size());
        assertEquals(1, commentsDtos.get(0).getCount());
        verify(commentRepository, times(1)).findByAdId(1L);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void addComment() {
        // Set up the security context
        UserDetails userDetails = User
                .withUsername("testuser@example.com")
                .password("password")
                .roles("USER")
                .build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CreateOrUpdateCommentDto createCommentDto = CreateOrUpdateCommentDto.builder()
                .text("Test Comment")
                .build();

        CommentDto savedCommentDto = commentsService.addComment(1L, createCommentDto);

        assertNotNull(savedCommentDto);
        assertEquals("Test Comment", savedCommentDto.getText());
        assertEquals(ConstantGeneratorFotTest.USER_ID, savedCommentDto.getAuthor());
        verify(adRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentsService.deleteComment(1L, 1L);

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void updateComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto updatedCommentDto = commentsService.updateComment(1L,
                1L,
                createOrUpdateCommentDto);

        assertNotNull(updatedCommentDto);
        assertEquals("Test Comment", updatedCommentDto.getText());
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
