package ru.skypro.homework.service;

import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

public interface CreateOrUpdateCommentService {
    boolean createOrUpdateComment(CreateOrUpdateComment comment);

    List<CreateOrUpdateComment> getComments();

    CreateOrUpdateComment getCommentById(Long id);

    boolean updateComment(Long id, CreateOrUpdateComment comment);

    boolean deleteComment(Long id);
}
