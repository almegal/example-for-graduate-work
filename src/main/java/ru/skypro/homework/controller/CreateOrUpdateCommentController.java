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

import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CreateOrUpdateCommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Комментарии", value = "API для управления комментариями")
public class CreateOrUpdateCommentController {

    private final CreateOrUpdateCommentService createOrUpdateCommentService;

    @ApiOperation(value = "Создание или обновление комментария",
            notes = "Позволяет пользователю создать или обновить комментарий",
            response = CreateOrUpdateComment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/create-or-update-comment")
    public ResponseEntity<String> createOrUpdateComment(@Valid @RequestBody CreateOrUpdateComment comment) {
        if (createOrUpdateCommentService.createOrUpdateComment(comment)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Сохранено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка");
        }
    }

    @ApiOperation(value = "Получение всех комментариев",
            notes = "Возвращает список всех комментариев",
            response = CreateOrUpdateComment.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/create-or-update-comments")
    public ResponseEntity<List<CreateOrUpdateComment>> getComments() {
        List<CreateOrUpdateComment> comments = createOrUpdateCommentService.getComments();
        return ResponseEntity.ok(comments);
    }

    @ApiOperation(value = "Получение комментария по ID",
            notes = "Возвращает комментарий по его ID",
            response = CreateOrUpdateComment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/create-or-update-comment/{id}")
    public ResponseEntity<CreateOrUpdateComment> getCommentById(@PathVariable Long id) {
        CreateOrUpdateComment comment = createOrUpdateCommentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Обновление комментария",
            notes = "Обновляет существующий комментарий",
            response = CreateOrUpdateComment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PutMapping("/create-or-update-comment/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateComment comment) {
        if (createOrUpdateCommentService.updateComment(id, comment)) {
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
    @DeleteMapping("/create-or-update-comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        if (createOrUpdateCommentService.deleteComment(id)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }
}