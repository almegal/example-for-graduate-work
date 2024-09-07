package ru.skypro.homework.dto;

import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;
import javax.validation.constraints.Size;
@Data
public class CommentsDto {

    @Size(min = 0, message = "Количество комментариев не может быть отрицательным")
    @ApiModelProperty(
            value = "Общее количество комментариев",
            example = OpenApiConstant.COUNT_COMMENTS)
    private Integer count;

    @Size(min = 0, message = "Список комментария не может быть отрицательным")
    @ApiModelProperty(
            value = "Список комментариев",
            example = OpenApiConstant.LIST_COMMENTS)
    private List<CommentDto> results;
}
