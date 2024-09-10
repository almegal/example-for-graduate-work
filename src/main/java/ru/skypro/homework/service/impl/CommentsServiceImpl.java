package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.service.CommentsService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    @Override
    public List<CommentsDto> getCommentsByAdId(Long adId) {
        return null;
    }

    @Override
    public CommentDto addComment(Long adId, CommentDto comment) {
        return null;
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {

    }

    @Override
    public CommentDto updateComment(Long adId, Long commentId, CommentDto comment) {
        return null;
    }
}
