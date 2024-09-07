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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;
import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Пользователи", value = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Обновление пароля",
            notes = "Позволяет пользователю обновить пароль",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            )
    })
    @PostMapping("/users/set_password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody NewPasswordDto newPassword) {
        if (userService.updatePassword(newPassword)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ApiOperation(value = "Получение информации об авторизованном пользователе",
            notes = "Возвращает информацию об авторизованном пользователе",
            response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"
            )
    })
    @GetMapping("/users/me")
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        UserDto user = userService.getAuthenticatedUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ApiOperation(value = "Обновление информации об авторизованном пользователе",
            notes = "Позволяет пользователю обновить свою информацию",
            response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"
            )
    })
    @PatchMapping("/users/me")
    public ResponseEntity<UserDto> updateAuthenticatedUserInfo(
            @Valid @RequestBody UpdateUserDto updateUser) {
        UserDto user = userService.updateAuthenticatedUserInfo(updateUser);
        if (updateUser != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ApiOperation(value = "Обновление аватара авторизованного пользователя",
            notes = "Позволяет пользователю обновить свой аватар",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"
            )
    })
    @PatchMapping("/users/me/image")
    public ResponseEntity<Void> updateAuthenticatedUserImage(
            @RequestParam("image") MultipartFile image) {
        if (userService.updateAuthenticatedUserImage(image)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
    }
}
