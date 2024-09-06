package ru.skypro.homework.service;


import ru.skypro.homework.dto.Comments;

import java.util.List;

public interface CommentsService {
    public boolean createComments(Comments comments);

    List<Comments> getAllComments();

    Comments getCommentsById(Long id);

    boolean updateComments(Long id, Comments comments);

    boolean deleteComments(Long id);

}
