package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.service.CommentsService;

import javax.validation.Valid;
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
            response = Comment.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/ads/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("id") Long adId) {
        List<Comment> comments = commentsService.getCommentsByAdId(adId);
        return ResponseEntity.ok(comments);
    }

    @ApiOperation(value = "Добавление комментария к объявлению",
            notes = "Позволяет добавить комментарий к указанному объявлению",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/ads/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Long adId,
                                              @Valid @RequestBody Comment comment) {
        Comment createdComment = commentsService.addComment(adId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @ApiOperation(value = "Удаление комментария",
            notes = "Позволяет удалить комментарий по его идентификатору",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "No Content"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") Long adId,
                                              @PathVariable("commentId") Long commentId) {
        commentsService.deleteComment(adId, commentId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Обновление комментария",
            notes = "Позволяет обновить комментарий по его идентификатору",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") Long adId,
                                                 @PathVariable("commentId") Long commentId,
                                                 @Valid @RequestBody Comment comment) {
        Comment updatedComment = commentsService.updateComment(adId, commentId, comment);
        return ResponseEntity.ok(updatedComment);
    }
}