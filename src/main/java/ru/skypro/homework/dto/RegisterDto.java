package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "Register", description = "Модель для регистрации пользователя")
public class RegisterDto {
    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.USER_NAME)
    private String username;
    @ApiModelProperty(value = "Пароль пользователя", example = OpenApiConstant.USER_PASS)
    private String password;
    @ApiModelProperty(value = "Имя", example = OpenApiConstant.NAME)
    private String firstName;
    @ApiModelProperty(value = "Фамилия", example = OpenApiConstant.LAST_NAME)
    private String lastName;
    @ApiModelProperty(value = "Номер телефона", example = OpenApiConstant.PHONE)
    private String phone;
    @ApiModelProperty(value = "Роль", example = "USER", allowableValues = "USER, ADMIN")
    private Role role;
}