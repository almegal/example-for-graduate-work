package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "UpdateUser", description = "Модель для обновления информации о пользователе")
public class UpdateUserDto {
    @NotBlank(message = "Имя не может быть пустым или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Имя должно быть от 3 до 10 символов")
    @ApiModelProperty(
            value = "Изменить имя пользователя",
            example = OpenApiConstant.NAME)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Фамилия должна быть от 3 до 10 символов")
    @ApiModelProperty(value = "Изменить фамилию пользователя")
    private String lastName;

    @NotBlank(message = "Телефон не может быть пустым или состоять только из пробелов")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен быть валидным номером в формате +7 (XXX) XXX-XX-XX"
    )
    @ApiModelProperty(value = "Изменить телефон пользователя", example = OpenApiConstant.PHONE)
    private String phone;
}
