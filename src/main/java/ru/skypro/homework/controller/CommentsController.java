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

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.service.CommentsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Списки комментариев", value = "API для управления списками комментариев")
public class CommentsController {

    private final CommentsService commentsService;

    @ApiOperation(value = "Создание списка комментариев",
            notes = "Позволяет пользователю создать новый список комментариев",
            response = Comments.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/comments-list")
    public ResponseEntity<String> createComments(@Valid @RequestBody Comments comments) {
        if (commentsService.createComments(comments)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Успешно");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка");
        }
    }

    @ApiOperation(value = "Получение всех списков комментариев",
            notes = "Возвращает список всех списков комментариев",
            response = Comments.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/comments-list")
    public ResponseEntity<List<Comments>> getAllComments() {
        List<Comments> comments = commentsService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @ApiOperation(value = "Получение списка комментариев по ID",
            notes = "Возвращает список комментариев по его ID",
            response = Comments.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/comments-list/{id}")
    public ResponseEntity<Comments> getCommentsById(@PathVariable Long id) {
        Comments comments = commentsService.getCommentsById(id);
        if (comments != null) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Обновление списка комментариев",
            notes = "Обновляет существующий список комментариев",
            response = Comments.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PutMapping("/comments-list/{id}")
    public ResponseEntity<String> updateComments(@PathVariable Long id, @Valid @RequestBody Comments comments) {
        if (commentsService.updateComments(id, comments)) {
            return ResponseEntity.ok().body("Успешно");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }

    @ApiOperation(value = "Удаление списка комментариев",
            notes = "Удаляет список комментариев по его ID",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @DeleteMapping("/comments-list/{id}")
    public ResponseEntity<String> deleteComments(@PathVariable Long id) {
        if (commentsService.deleteComments(id)) {
            return ResponseEntity.ok().body("Успешно");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }
}