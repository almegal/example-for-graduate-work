package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentsService {

    CommentsDto getCommentsByAdId(Long adId);

    CommentDto addComment(Long adId, CreateOrUpdateCommentDto createCommentDto);

    void deleteComment(Long adId, Long commentId);

    CommentDto updateComment(Long adId,
                             Long commentId,
                             CreateOrUpdateCommentDto createOrUpdateCommentDto);

}
