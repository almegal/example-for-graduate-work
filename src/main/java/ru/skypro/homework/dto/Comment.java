package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class Comment {
    @NotNull(message = "ID автора комментария не может быть Null")
    @ApiModelProperty(value = "ID автора комментария", example = OpenApiConstant.ID)
    private Integer author;

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Фото автора должно быть URL"
    )
    @ApiModelProperty(value = "Фото автора комментария", example = OpenApiConstant.IMAGE)
    private String authorImage;

    @NotBlank(message = "Поле не может быть пустым или состоять только из пробелов")
    @Size(min = 3, max = 16, message = "Имя должен быть от 3 до 16 символов")
    @ApiModelProperty(value = "Имя автора комментария", example = OpenApiConstant.NAME)
    private String authorFirstName;

    @NotBlank(message = "Дата создания комментария не может быть null")
    @ApiModelProperty(value = "Дата создания комментария в формате yyyyMMddHHmmss", example = OpenApiConstant.DATE)
    private Long createdAt;

    @NotNull(message = "ID комментария не может быть Null")
    @ApiModelProperty(value = "ID комментария", example = OpenApiConstant.ID)
    private Integer pk;

    @NotBlank(message = "Текст комментария не может быть пустым или состоять только из пробелов")
    @Size(min = 1, max = 255, message = "Текст комментарии должно быть от 1 до 255 символов")
    @ApiModelProperty(value = "Текст коментария", example = OpenApiConstant.TEXT)
    private String text;
    private Long id;

}
