package ru.skypro.homework.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "User", description = "Модель пользователя")
public class UserDto {

    @NotNull(message = "ID не может быть null")
    @ApiModelProperty(
            value = "Id пользователя",
            example = OpenApiConstant.ID)
    private Integer id;

    @NotBlank(message = "Email не может быть пустым или состоять только из пробелов")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email должен быть валидным адресом электронной почты"
    )
    @ApiModelProperty(
            value = "Email пользователя",
            example = "user@example.com")
    private String email;

    @NotBlank(message = "Имя не может быть пустым или состоять только из пробелов")
    @Size(min = 1, max = 50, message = "Имя должно быть от 1 до 50 символов")
    @ApiModelProperty(
            value = "Имя пользователя",
            example = OpenApiConstant.NAME)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой или состоять только из пробелов")
    @Size(min = 1, max = 50, message = "Фамилия должна быть от 1 до 50 символов")
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

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Фото пользователя должно быть URL"
    )
    @ApiModelProperty(
            value = "Ссылка на аватар пользователя",
            example = OpenApiConstant.IMAGE)
    private String image;
}
