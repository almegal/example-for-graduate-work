package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Авторизация", value = "api позволяет авторизоваться пользователю")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "Авторизация по логину и паролю",
            notes = "Позволяет пользователю войти в систему",
            response = LoginDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @PostMapping("/login")
    public boolean login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }
}
