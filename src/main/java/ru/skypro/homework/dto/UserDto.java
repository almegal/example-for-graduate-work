package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "User", description = "Модель пользователя")
@Builder
public class UserDto {

    @NotNull(message = "ID не может быть null")
    @ApiModelProperty(
            value = "Id пользователя",
            example = OpenApiConstant.ID)
    private Long id;

    @NotBlank(message = "Email не может быть пустым или состоять только из пробелов")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email должен быть валидным адресом электронной почты"
    )
    @ApiModelProperty(
            value = "Email пользователя",
            example = "user@example.com")
    @Size(min = 8, max = 32)
    private String email;

    @NotBlank(message = "Имя не может быть пустым или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Имя должно быть от 3 до 10 символов")
    @ApiModelProperty(
            value = "Имя пользователя",
            example = OpenApiConstant.NAME)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Фамилия должна быть от 3 до 10 символов")
    @ApiModelProperty(
            value = "Фамилия пользователя",
            example = OpenApiConstant.LAST_NAME)
    private String lastName;

    @NotBlank(message = "Телефон должен быть валидным номером в формате +7 (XXX) XXX-XX-XX")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен быть валидным номером")
    @ApiModelProperty(
            value = "Телефон пользователя",
            example = OpenApiConstant.PHONE)
    private String phone;

    @NotNull(message = "Роль не может быть null")
    @ApiModelProperty(
            value = "Роль пользователя",
            example = "USER")
    private Role role;

    @ApiModelProperty(
            value = "Ссылка на аватар пользователя",
            example = OpenApiConstant.IMAGE)
    private String image;
}
