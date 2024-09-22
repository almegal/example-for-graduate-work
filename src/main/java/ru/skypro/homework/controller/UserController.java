package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void updatePassword(
            @Valid @RequestBody NewPasswordDto newPassword) {
        userService.updatePassword(newPassword);
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
    public UserDto getAuthenticatedUser() {
        return userService.getAuthenticatedUser();
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
    public UserDto updateAuthenticatedUserInfo(
            @Valid @RequestBody UpdateUserDto updateUser) {
        return userService.updateAuthenticatedUserInfo(updateUser);
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
    public void updateAuthenticatedUserImage(
            @RequestParam("image") MultipartFile image) {
        userService.updateAuthenticatedUserImage(image);
    }
}
