package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@ApiModel(value = "Register", description = "Модель для регистрации пользователя")
public class RegisterDto {

    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.USER_NAME)
    @NotBlank(message = "Username is mandatory")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email должен быть валидным адресом электронной почты"
    )
    @Size(min = 8, max = 32)
    private String username;

    @ApiModelProperty(value = "Пароль пользователя", example = OpenApiConstant.USER_PASS)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 16)
    private String password;

    @ApiModelProperty(value = "Имя", example = OpenApiConstant.NAME)
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 10, message = "Имя должно быть от 3 до 10 символов")
    private String firstName;

    @ApiModelProperty(value = "Фамилия", example = OpenApiConstant.LAST_NAME)
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, max = 10, message = "Фамилия должна быть от 3 до 10 символов")
    private String lastName;

    @ApiModelProperty(value = "Номер телефона", example = OpenApiConstant.PHONE)
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен быть валидным номером в формате +7 (XXX) XXX-XX-XX"
    )
    private String phone;

    @ApiModelProperty(value = "Роль", example = "USER", allowableValues = "USER, ADMIN")
    @NotNull(message = "Role is mandatory")
    private Role role;
}
