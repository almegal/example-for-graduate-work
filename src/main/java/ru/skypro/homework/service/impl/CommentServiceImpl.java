package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.service.CommentService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public boolean createComment(Comment comment) {
        return true;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }

    @Override
    public Comment getCommentById(Long id) {
        return null;
    }

    @Override
    public boolean updateComment(Long id, Comment comment) {
        return false;
    }

    @Override
    public boolean deleteComment(Long id) {
        return false;
    }
}
