package ru.skypro.homework.dto;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "Ads", description = "Модель списка объявлений")
public class AdsDto {

    @Size(min = 0, message = "Количество объявлений не может быть отрицательным")
    @ApiModelProperty(value = "Общее количество объявлений", example = OpenApiConstant.COUNT_ADS)
    private Integer count;

    @Size(min = 0, message = "Список объявлений не может быть отрицательным")
    @ApiModelProperty(value = "Список объявлений", example = OpenApiConstant.LIST_ADS)
    private List<AdDto> results;


}
