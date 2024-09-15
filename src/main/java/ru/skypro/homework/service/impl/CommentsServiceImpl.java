package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.Repository.CommentRepository;
import ru.skypro.homework.controller.CommentsController;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentsService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    @Override
    public List<CommentsDto> getCommentsByAdId(Long adId) {
        List<Comment> commentList = commentRepository.findByAdId(adId);
        List<CommentDto> commentDto = commentMapper.toDtos(commentList);
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentDto.size());
        commentsDto.setResults(commentDto);
        return List.of(commentsDto);
    }

    @Override
    public CommentDto addComment(Long adId, CommentDto commentDto) {
        CreateOrUpdateCommentDto createCommentDto = CreateOrUpdateCommentDto.builder()
                .text(commentDto.getText())
                .build();
        Comment comment = commentMapper.toEntity(createCommentDto);
        Ad ad = commentRepository.findAdById(adId);
        if (ad == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        User author = commentRepository.findAuthorById(commentDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("Комментарий не найден"));
        if (!comment.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Комментарий не принадлежит данному объявлению");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));
        if (!comment.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Комментарий не принадлежит данному объявлению");
        }
        commentMapper.toEntityFromCreateUpdatDto(updateCommentDto,comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }
}
