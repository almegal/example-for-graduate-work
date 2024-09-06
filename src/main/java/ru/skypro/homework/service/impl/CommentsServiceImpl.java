package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.service.CommentsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    @Override
    public boolean createComments(Comments comments) {
        return true;
    }

    @Override
    public List<Comments> getAllComments() {
        return null;
    }

    @Override
    public Comments getCommentsById(Long id) {
        return null;
    }

    @Override
    public boolean updateComments(Long id, Comments comments) {
        return true;
    }

    @Override
    public boolean deleteComments(Long id) {
        return true;
    }
}