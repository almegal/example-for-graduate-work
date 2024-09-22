package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentsService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Комментарии", value = "API для управления комментариями")
public class CommentsController {

    private final CommentsService commentsService;

    @ApiOperation(value = "Получение комментариев объявления",
            notes = "Возвращает список комментариев для указанного объявления",
            response = CommentsDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping("/ads/{id}/comments")
    public CommentsDto getComments(@PathVariable("id") Long adId) {
        CommentsDto commentsDto = commentsService.getCommentsByAdId(adId);
        if (commentsDto.getCount() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарии не найдены");
        }
        return commentsDto;
    }

    @ApiOperation(value = "Добавление комментария к объявлению",
            notes = "Позволяет добавить комментарий к указанному объявлению",
            response = CommentDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PostMapping("/ads/{id}/comments")
    public CommentDto addComment(
            @PathVariable("id") Long adId,
            @Valid @RequestBody CreateOrUpdateCommentDto createCommentDto) {
        return commentsService.addComment(adId, createCommentDto);
    }

    @ApiOperation(value = "Удаление комментария",
            notes = "Позволяет удалить комментарий по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable("adId") Long adId,
                              @PathVariable("commentId") Long commentId) {
        try {
            commentsService.deleteComment(adId, commentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарий не найден");
        }
    }

    @ApiOperation(value = "Обновление комментария",
            notes = "Позволяет обновить комментарий по его идентификатору",
            response = CommentDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public CommentDto updateComment(
            @PathVariable("adId") Long adId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CreateOrUpdateCommentDto updateCommentDto) {

        return commentsService.updateComment(adId, commentId, updateCommentDto);
    }
}
