package ru.skypro.homework.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.Repository.AdRepository;
import ru.skypro.homework.Repository.CommentRepository;
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
        User user = userRepository
                .findByEmail(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        Comment comment = new Comment();
        comment.setText(createCommentDto.getText());
        comment.setAd(ad);
        comment.setAuthor(user);
        comment.setCreatedAt(System.currentTimeMillis());

        comment = commentRepository.save(comment);

        return commentMapper.toDto(comment);
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
