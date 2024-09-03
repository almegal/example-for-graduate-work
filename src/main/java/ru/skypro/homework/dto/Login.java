package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "Login", description = "Модель пользователя")
public class Login {

    @ApiModelProperty(value = "Пароль", example = OpenApiConstant.USER_PASS)
    @Size(min = 4, max = 32)
    private String password;
    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.USER_NAME)
    @Size(min = 8, max = 16)
    private String username;
}
