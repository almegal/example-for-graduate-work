package ru.skypro.homework.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
public class CreateOrUpdateCommentDto {
    @NotBlank
    @Size(min = 8, max = 64)
    @ApiModelProperty(value = "Текст комментария", example = OpenApiConstant.TEXT)
    private String text;
}
