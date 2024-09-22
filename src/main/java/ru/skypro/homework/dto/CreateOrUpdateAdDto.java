package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "CreateOrUpdateAd", description = "Модель создания или обновления объявления")
public class CreateOrUpdateAdDto {

    @NotBlank(message = "Заголовок не может быть пустым или состоять только из пробелов")
    @Size(min = 4, max = 32, message = "Заголовок должен быть от 4 до 32 символов")
    @ApiModelProperty(value = "Создание или обновление заголовка объявления",
            example = OpenApiConstant.TITLE)
    private String title;

    @NotNull(message = "Цена не может быть пустой или состоять только из пробелов")
    @Min(value = 0, message = "Цена не может быть меньше 0")
    @Max(value = 10_000_000, message = "Цена не может превышать 10 000 00")
    @ApiModelProperty(value = "Создание или обновление цены объявления",
            example = OpenApiConstant.PRICE)
    private Integer price;

    @NotBlank(message = "Описание не может быть пустым или состоять только из пробелов")
    @Size(min = 8, max = 64, message = "Описание должно быть от 8 до 64 символов")
    @ApiModelProperty(value = "Создание или обновление описания объявления",
            example = OpenApiConstant.DESCRIPTION)
    private String description;


}
