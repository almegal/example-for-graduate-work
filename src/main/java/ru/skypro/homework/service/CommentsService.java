package ru.skypro.homework.service;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import java.util.List;

public interface CommentsService {

    /**
     * Получение всех комментариев по идентификатору объявления
     * Метод использует:
     * {@link CommentRepository#findByAdId(Long)}
     * {@link CommentMapper#toDtos(List)}
     * {@link CommentsDto#setCount(Integer)}
     * {@link CommentsDto#setResults(List)}
     * @param adId - идентификатор пользователя
     * @return AdsDto - модель списка объявлений {@link AdsDto}
     */
    CommentsDto getCommentsByAdId(Long adId);

    /**
     * Создание комментария
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link AdService#findAdById(Long)}
     * {@link CommentMapper#toEntityFromCreateUpdatDto(CreateOrUpdateCommentDto, Comment)}
     * {@link Comment#setAd(Ad)}
     * * {@link Comment#setAuthor(User)}
     * {@link CommentRepository#save(Object)}
     * {@link CommentMapper#toDto(Comment)}
     * @param adId - идентификатор объявления
     * @param createCommentDto - модель для создания или обновления комментария
     * @return CommentDTO - модель объявления {@link CommentDto}
     */
    CommentDto addComment(Long adId, CreateOrUpdateCommentDto createCommentDto);

    /**
     * Удаление комментария
     * Метод использует:
     * {@link CommentRepository#findById(Object)}
     * {@link CommentRepository#delete(Object)}
     * @param adId - идентификатор объявления
     * @param commentId - идентификатор комментария
     */
    void deleteComment(Long adId, Long commentId);

    /**
     * Обновление комментария
     * Метод использует:
     * {@link CommentRepository#findById(Object)}
     * {@link CommentMapper#toEntityFromCreateUpdatDto(CreateOrUpdateCommentDto, Comment)}
     * {@link CommentRepository#save(Object)}
     * {@link CommentMapper#toDto(Comment)}
     * @param adId - идентификатор объявления
     * @param commentId - идентификатор комментария
     * @param createOrUpdateCommentDto - модель для создания или обновления комментария
     */
    CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    /**
     * Проверка, является ли пользователь создателем комментария или администратором
     * Метод использует:
     * {@link CommentRepository#findById(Object)}
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link Role#ADMIN
     * {@link Comment#getAuthor()}
     * {@link User#getEmail()}
     * @param commentId - идентификатор комментария
     * @throws NotFoundException - исключение выбрасывается, если комментарий не найден в базе данных
     * @return true - если пользователь является создателем комментария или админом; false - в противоположном случае
     */
    boolean isCommentCreatorOrAdmin(Long commentId);

}
