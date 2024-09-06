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

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.service.UpdateUserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Пользователи", value = "API для управления пользователями")
public class UpdateUserController {

    private final UpdateUserService updateUserService;

    @ApiOperation(value = "Обновление пользователя",
            notes = "Позволяет пользователю обновить свои данные",
            response = UpdateUser.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/update-user")
    public ResponseEntity<String> createUpdateUser(@Valid @RequestBody UpdateUser updateUser) {
        if (updateUserService.createUpdateUser(updateUser)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Сохранено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка");
        }
    }

    @ApiOperation(value = "Получение всех пользователей",
            notes = "Возвращает список всех пользователей",
            response = UpdateUser.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/update-users")
    public ResponseEntity<List<UpdateUser>> getUpdateUsers() {
        List<UpdateUser> updateUsers = updateUserService.getUpdateUsers();
        return ResponseEntity.ok(updateUsers);
    }

    @ApiOperation(value = "Получение пользователя по ID",
            notes = "Возвращает пользователя по его ID",
            response = UpdateUser.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @GetMapping("/update-user/{id}")
    public ResponseEntity<UpdateUser> getUpdateUserById(@PathVariable Long id) {
        UpdateUser updateUser = updateUserService.getUpdateUserById(id);
        if (updateUser != null) {
            return ResponseEntity.ok(updateUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Обновление пользователя",
            notes = "Обновляет существующего пользователя",
            response = UpdateUser.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @PutMapping("/update-user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUser updateUser) {
        if (updateUserService.updateUser(id, updateUser)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }

    @ApiOperation(value = "Удаление пользователя",
            notes = "Удаляет пользователя по его ID",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not Found")
    })
    @DeleteMapping("/update-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (updateUserService.deleteUser(id)) {
            return ResponseEntity.ok().body("Без ошибок");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка");
        }
    }
}