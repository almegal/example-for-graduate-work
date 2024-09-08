package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;
import javax.validation.constraints.*;

@Data
@ApiModel(value = "Ad", description = "Модель объявления")
public class AdDto {

    @NotNull(message = "ID автора объявления не может быть null")
    @ApiModelProperty(value = "ID автора объявления", example = OpenApiConstant.ID)
    private Integer author;

    @ApiModelProperty(value = "Ссылка на картинку объявления", example = OpenApiConstant.AD_IMAGE)
    private String image;

    @NotNull(message = "ID объявления не может быть null")
    @ApiModelProperty(value = "ID объявления", example = OpenApiConstant.ID)
    private Integer pk;

    @Min(value = 0, message = "Цена не может быть меньше 0")
    @Max(value = 10_000_000, message = "Цена не может превышать 10 000 00")
    @ApiModelProperty(value = "Цена объявления", example = OpenApiConstant.PRICE)
    private Integer price;

    @Size(min = 4, max = 32, message = "Заголовок должен быть от 4 до 32 символов")
    @ApiModelProperty(value = "Заголовок объявления", example = OpenApiConstant.TITLE)
    private String title;


}
