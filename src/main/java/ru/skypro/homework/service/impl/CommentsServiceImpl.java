package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;

import ru.skypro.homework.service.CommentsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {


    @Override
    public List<Comment> getCommentsByAdId(Long adId) {
        return null;
    }

    @Override
    public Comment addComment(Long adId, Comment comment) {
        return null;
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {

    }

    @Override
    public Comment updateComment(Long adId, Long commentId, Comment comment) {
        return null;
    }
}