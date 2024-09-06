package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;

import java.util.List;

public interface CommentService {
    boolean createComment(Comment comment);

    List<Comment> getComments();

    Comment getCommentById(Long id);

    boolean updateComment(Long id, Comment comment);

    boolean deleteComment(Long id);
}
