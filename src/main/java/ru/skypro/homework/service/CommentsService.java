package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import java.util.List;
public interface CommentsService {

    List<CommentsDto> getCommentsByAdId(Long adId);

    CommentDto addComment(Long adId, CommentDto comment);

    void deleteComment(Long adId, Long commentId);

    CommentDto updateComment(Long adId, Long commentId, CommentDto comment);
}
