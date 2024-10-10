package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.ImageUploadService;
import ru.skypro.homework.service.UserService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Пользователи", value = "API для управления пользователями")
public class UserController {

    private final UserService userService;
    private final ImageUploadService imageUploadService;


    @ApiOperation(value = "Обновление пароля",
            notes = "Позволяет пользователю обновить пароль",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request"),
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
    public boolean updatePassword(@Valid @RequestBody NewPasswordDto newPassword) {
        return userService.updatePassword(newPassword);
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
    public UserDto updateAuthenticatedUserInfo(@Valid @RequestBody UpdateUserDto updateUser) {
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
    public UserDto updateAuthenticatedUserImage(@RequestParam("image") MultipartFile file) {
        return userService.updateAuthenticatedUserImage(file);
    }


    @ApiOperation(value = "Выгрузка картинки из файловой системы в профиль пользователя",
            notes = "Позволяет выгрузить картинку из файловой системы")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping(value = "/{imagePath}")
    public ResponseEntity<byte[]> downloadImageForUser(@PathVariable String imagePath) {
        byte[] image = imageUploadService.getImageByAdId(imagePath);
        // filePath, извлеченный из User, содержит только "/" и имя файла (без имени папки)
        // в методе сервиса к нему добавляется имя папки и "/"
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    // Для того чтобы получить картинку из файловой системы, фронтенд обращается по адресу: localhost:8080
    // и в качестве переменной передает в пути еще и имя файла картинки (путь к файлу без имени папки).
    // Мы удаляем имя папки из пути к файлу по той причине, чтобы при подставлении фронтендом значения "путь
    // к файлу" в путь localhost:8080, избежать получения эндпоинта: /images, которого в действительности нет.

}
