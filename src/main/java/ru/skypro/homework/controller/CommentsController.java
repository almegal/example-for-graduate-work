package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentsService;

import java.util.List;

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
    public ResponseEntity<CommentsDto> getComments(@PathVariable("id") Long adId) {
        List<CommentsDto> commentsList = commentsService.getCommentsByAdId(adId);
        if (commentsList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарии не найдены");
        }
        return ResponseEntity.ok(commentsList.get(0));
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
    public ResponseEntity<CommentDto> addComment(
            @PathVariable("id") Long adId,
            @Valid @RequestBody CreateOrUpdateCommentDto createCommentDto) {
        CommentDto commentDto = commentsService.addComment(adId, createCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
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
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") Long adId,
                                              @PathVariable("commentId") Long commentId) {
        try {
            commentsService.deleteComment(adId, commentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарий не найден");
        }
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("adId") Long adId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CreateOrUpdateCommentDto updateCommentDto) {
        CommentDto commentDto;
        try {
            commentDto = commentsService.updateComment(adId, commentId, updateCommentDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарий не найден");
        }
        return ResponseEntity.ok(commentDto);
    }
}
