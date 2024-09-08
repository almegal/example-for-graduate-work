package ru.skypro.homework.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(
        value = "CreateOrUpdateComment",
        description = "Модель для создания или обновления комментария")
public class CreateOrUpdateCommentDto {
    @NotBlank
    @Size(min = 8, max = 64)
    @ApiModelProperty(value = "Текст комментария", example = OpenApiConstant.TEXT)
    private String text;
}
