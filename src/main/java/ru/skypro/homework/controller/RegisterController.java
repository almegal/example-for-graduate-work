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
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Регистрация", value = "api создать нового пользователя")
public class RegisterController {

    private final AuthService authService;

    @ApiOperation(value = "Регистрация пользователя",
            notes = "Позволяет пользователю пройти регистрацию",
            response = RegisterDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request")
    })
    @PostMapping("/register")
    public boolean register(@Valid @RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
}
