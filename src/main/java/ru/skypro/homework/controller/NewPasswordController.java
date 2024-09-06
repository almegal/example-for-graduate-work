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

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.service.NewPasswordService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Пароли", value = "API для управления паролями")
public class NewPasswordController {

    private final NewPasswordService newPasswordService;

    @ApiOperation(value = "Создание нового пароля",
            notes = "Позволяет пользователю создать новый пароль",
            response = NewPassword.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/new-password")
    public ResponseEntity<String> createNewPassword(@Valid @RequestBody NewPassword newPassword) {
        if (newPasswordService.createNewPassword(newPassword)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Сохранено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка");
        }
    }

    @ApiOperation(value = "Получение всех паролей",
            notes = "Возвращает список всех паролей",
            response = NewPassword.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/new-passwords")
    public ResponseEntity<List<NewPassword>> getNewPasswords() {
        List<NewPassword> newPasswords = newPasswordService.getNewPasswords();
        return ResponseEntity.ok(newPasswords);
    }

    @ApiOperation(value = "Получение пароля по ID",
            notes = "Возвращает пароль по его ID",
            response = NewPassword.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/new-password/{id}")
    public ResponseEntity<NewPassword> getNewPasswordById(@PathVariable Long id) {
        NewPassword newPassword = newPasswordService.getNewPasswordById(id);
        if (newPassword != null) {
            return ResponseEntity.ok(newPassword);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Обновление пароля",
            notes = "Обновляет существующий пароль",
            response = NewPassword.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PutMapping("/new-password/{id}")
    public ResponseEntity<String> updateNewPassword(@PathVariable Long id, @Valid @RequestBody NewPassword newPassword) {
        if (newPasswordService.updateNewPassword(id, newPassword)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }

    @ApiOperation(value = "Удаление пароля",
            notes = "Удаляет пароль по его ID",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @DeleteMapping("/new-password/{id}")
    public ResponseEntity<String> deleteNewPassword(@PathVariable Long id) {
        if (newPasswordService.deleteNewPassword(id)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }
}