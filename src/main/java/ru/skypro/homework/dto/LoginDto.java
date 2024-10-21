package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "Login", description = "Модель для аутентификации пользователя")
public class LoginDto {

    @ApiModelProperty(value = "Пароль", example = OpenApiConstant.USER_PASS)
    private String password;

    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.USER_NAME)
    private String username;
}
