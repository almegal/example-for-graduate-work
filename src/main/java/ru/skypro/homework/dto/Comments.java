package ru.skypro.homework.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Scope;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Comments {

    @Size(min = 0, message = "Количество комментариев не может быть отрицательным")
    @ApiModelProperty(value = "Количество комментариев", example = OpenApiConstant.COUNT_COMMENTS)
    private Integer count;

    @Size(min = 1, message = "Список комментария не может быть пустым")
    @ApiModelProperty(value = "Список комментариев", example = OpenApiConstant.LIST_COMMENTS)
    private List<Comment> results;
}
