package ru.skypro.homework.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrUpdateAd {
    @Size(min = 4, max = 32)
    private String title;
    @Min(value = 0, message = "Цена не может быть меньше 0")
    @Max(value = 10_000_000, message = "Не может превышать 10 000 00")
    private Integer price;
    @Size(min = 8, max = 64)
    private String description;
}
