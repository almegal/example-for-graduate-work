package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.Repository.CommentRepository;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.IllegalArgumentException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
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
    public CommentDto addComment(Long adId, CreateOrUpdateCommentDto createCommentDto) {
        Comment comment = commentMapper.toEntity(createCommentDto);
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        // Получаем текущего пользователя из контекста безопасности
        String username = getCurrentUsername();
        User author = userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));
        if (!comment.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Комментарий не принадлежит данному объявлению");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(Long adId,
                                    Long commentId,
                                    CreateOrUpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));
        if (!comment.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Комментарий не принадлежит данному объявлению");
        }
        commentMapper.toEntityFromCreateUpdatDto(updateCommentDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
