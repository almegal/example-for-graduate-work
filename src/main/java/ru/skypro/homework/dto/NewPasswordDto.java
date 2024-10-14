package ru.skypro.homework.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "NewPassword", description = "Модель для обновления пароля")
public class NewPasswordDto {

    @NotNull(message = "Текущий пароль не может быть Null")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    @ApiModelProperty(
            value = "Текущий пароль",
            example = OpenApiConstant.CURRENT_PASSWORD)
    private String currentPassword;

    @NotNull(message = "Новый пароль не может быть Null")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    @ApiModelProperty(
            value = "Новый пароль",
            example = OpenApiConstant.NEW_PASSWORD)
    private String newPassword;
}
