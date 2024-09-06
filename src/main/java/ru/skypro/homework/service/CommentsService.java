package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;

import java.util.List;

public interface CommentsService {


    List<Comment> getCommentsByAdId(Long adId);

    Comment addComment(Long adId, Comment comment);

    void deleteComment(Long adId, Long commentId);

    Comment updateComment(Long adId, Long commentId, Comment comment);
}
