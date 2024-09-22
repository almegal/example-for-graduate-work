package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "Ad", description = "Модель объявления")
public class AdDto {

    @NotNull(message = "ID автора объявления не может быть null")
    @ApiModelProperty(value = "ID автора объявления", example = OpenApiConstant.ID)
    private Long author;

    @ApiModelProperty(value = "Путь к картинке объявления", example = OpenApiConstant.AD_IMAGE)
    private String image;

    // Согласно описанию апи мы сами задаем айди
    // не генерируем его?
    @NotNull(message = "ID объявления не может быть null")
    @ApiModelProperty(value = "ID объявления", example = OpenApiConstant.AD_ID)
    private Long pk;

    @Min(value = 0, message = "Цена не может быть меньше 0")
    @Max(value = 10_000_000, message = "Цена не может превышать 10 000 00")
    @ApiModelProperty(value = "Цена объявления", example = OpenApiConstant.PRICE)
    private Integer price;

    @Size(min = 4, max = 32, message = "Заголовок должен быть от 4 до 32 символов")
    @ApiModelProperty(value = "Заголовок объявления", example = OpenApiConstant.TITLE)
    private String title;


}
