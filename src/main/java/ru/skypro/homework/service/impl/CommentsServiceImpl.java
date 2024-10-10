package ru.skypro.homework.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentsService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final AdService adService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final SecurityServiceImpl securityService;

    @Override
    @Transactional(readOnly = true)
    public CommentsDto getCommentsByAdId(Long adId) {
        List<Comment> commentList = commentRepository.findByAdId(adId);
        List<CommentDto> commentDtoList = commentMapper.toDtos(commentList);
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentDtoList.size());
        commentsDto.setResults(commentDtoList);
        return commentsDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CommentDto addComment(Long adId, CreateOrUpdateCommentDto createCommentDto) {
        String userName = securityService.getAuthenticatedUserName();
        User user = userService.getUserByEmailFromDb(userName);
        Ad ad = adService.findAdById(adId);

        Comment comment = new Comment();
        commentMapper.toEntityFromCreateUpdatDto(createCommentDto, comment);
        comment.setAd(ad);
        comment.setAuthor(user);

        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    @PreAuthorize("@commentsServiceImpl.isCommentCreatorOrAdmin(#commentId)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteComment(Long adId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        commentRepository.delete(comment);
    }

    @Override
    @PreAuthorize("@commentsServiceImpl.isCommentCreatorOrAdmin(#commentId)")
    @Transactional
    public CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        commentMapper.toEntityFromCreateUpdatDto(updateCommentDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public boolean isCommentCreatorOrAdmin(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Комментарий не найден"));
        String email = securityService.getAuthenticatedUserName();
        User user = userService.getUserByEmailFromDb(email);
        return user.getRole() == Role.ADMIN || email.equals(comment.getAuthor().getEmail());
    }
}
