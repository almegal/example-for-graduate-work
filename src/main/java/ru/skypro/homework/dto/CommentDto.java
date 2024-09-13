package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "Comment", description = "Модель комментария")
public class CommentDto {
    @NotNull(message = "ID автора комментария не может быть Null")
    @ApiModelProperty(
            value = "ID автора комментария",
            example = OpenApiConstant.ID)
    private Long author;

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Ссылка на аватар автора комментария"
    )
    @ApiModelProperty(
            value = "Ссылка на аватар автора комментария",
            example = OpenApiConstant.IMAGE)
    private String authorImage;

    @NotBlank(message = "Поле не может быть пустым или состоять только из пробелов")
    @Size(min = 3, max = 16, message = "Имя должен быть от 3 до 16 символов")
    @ApiModelProperty(
            value = "Имя автора комментария",
            example = OpenApiConstant.NAME)
    private String authorFirstName;

    @NotBlank(message = "Дата создания комментария не может быть null")
    @ApiModelProperty(
            value = "Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970",
            example = OpenApiConstant.DATE)
    private Long createdAt;

    @NotNull(message = "ID комментария не может быть Null")
    @ApiModelProperty(
            value = "ID комментария",
            example = OpenApiConstant.ID)
    private Long pk;

    @NotBlank(message = "Текст комментария не может быть пустым или состоять только из пробелов")
    @Size(min = 1, max = 255, message = "Текст комментарии должно быть от 1 до 255 символов")
    @ApiModelProperty(
            value = "Текст комментария",
            example = OpenApiConstant.TEXT)
    private String text;

}
