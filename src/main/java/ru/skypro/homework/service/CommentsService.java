package ru.skypro.homework.service;

import java.util.List;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentsService {

    List<CommentsDto> getCommentsByAdId(Long adId);

    CommentDto addComment(Long adId, CreateOrUpdateCommentDto createCommentDto);

    void deleteComment(Long adId, Long commentId);

    CommentDto updateComment(Long adId,
                             Long commentId,
                             CreateOrUpdateCommentDto createOrUpdateCommentDto);

}
