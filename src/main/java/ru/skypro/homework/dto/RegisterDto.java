package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(value = "Register", description = "Модель для регистрации пользователя")
public class RegisterDto {

    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.USER_NAME)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @ApiModelProperty(value = "Пароль пользователя", example = OpenApiConstant.USER_PASS)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ApiModelProperty(value = "Имя", example = OpenApiConstant.NAME)
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @ApiModelProperty(value = "Фамилия", example = OpenApiConstant.LAST_NAME)
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @ApiModelProperty(value = "Номер телефона", example = OpenApiConstant.PHONE)
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

    @ApiModelProperty(value = "Роль", example = "USER", allowableValues = "USER, ADMIN")
    @NotNull(message = "Role is mandatory")
    private Role role;
}
