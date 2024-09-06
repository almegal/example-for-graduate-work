package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CreateOrUpdateCommentService;

import java.util.List;
@Service
public class CreateOrUpdateCommentServiceImpl implements CreateOrUpdateCommentService {
    @Override
    public boolean createOrUpdateComment(CreateOrUpdateComment comment) {
        return false;
    }

    @Override
    public List<CreateOrUpdateComment> getComments() {
        return null;
    }

    @Override
    public CreateOrUpdateComment getCommentById(Long id) {
        return null;
    }

    @Override
    public boolean updateComment(Long id, CreateOrUpdateComment comment) {
        return false;
    }

    @Override
    public boolean deleteComment(Long id) {
        return false;
    }
}
