package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.Repository.CommentRepository;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    private Comment comment;
    private CommentDto commentDto;
    private CreateOrUpdateCommentDto createOrUpdateCommentDto;
    private Ad ad;
    private User user;

    @BeforeEach
    void setUp() {
        ad = new Ad();
        ad.setId(1L);  // Используем Integer для id

        user = ConstantGeneratorFotTest.userGenerator();

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAd(ad);  // Устанавливаем объект Ad
        comment.setAuthor(user);  // Устанавливаем объект User

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
    void addComment() {
        when(commentRepository.findAdById(1L)).thenReturn(ad);
        when(commentRepository.findAuthorById(ConstantGeneratorFotTest.USER_ID)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toEntity(any(CreateOrUpdateCommentDto.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto savedCommentDto = commentsService.addComment(1L, commentDto);

        assertNotNull(savedCommentDto);
        assertEquals("Test Comment", savedCommentDto.getText());
        verify(commentRepository, times(1)).findAdById(1L);
        verify(commentRepository, times(1)).findAuthorById(ConstantGeneratorFotTest.USER_ID);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentsService.deleteComment(1L, 1L);  // Используем Long для adId

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void updateComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto updatedCommentDto = commentsService.updateComment(1L, 1L, createOrUpdateCommentDto);  // Используем Long для adId

        assertNotNull(updatedCommentDto);
        assertEquals("Test Comment", updatedCommentDto.getText());
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}