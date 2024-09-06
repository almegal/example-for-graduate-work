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
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Комментарии", value = "API для управления комментариями")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "Создание комментария",
            notes = "Позволяет пользователю создать новый комментарий",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/comments")
    public ResponseEntity<String> createComment(@Valid @RequestBody Comment comment) {
        if (commentService.createComment(comment)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Сохранено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка");
        }
    }

    @ApiOperation(value = "Получение всех комментариев",
            notes = "Возвращает список всех комментариев",
            response = Comment.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> comments = commentService.getComments();
        return ResponseEntity.ok(comments);
    }

    @ApiOperation(value = "Получение комментария по ID",
            notes = "Возвращает комментарий по его ID",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Обновление комментария",
            notes = "Обновляет существующий комментарий",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PutMapping("/comments/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        if (commentService.updateComment(id, comment)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }

    @ApiOperation(value = "Удаление комментария",
            notes = "Удаляет комментарий по его ID",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        if (commentService.deleteComment(id)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }
}