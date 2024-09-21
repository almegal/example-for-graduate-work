package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "ExtendedAd", description = "Модель полного объявления")
public class ExtendedAdDto {

    @NotNull(message = "ID объявления не может быть null")
    @ApiModelProperty(value = "ID объявления", example = OpenApiConstant.AD_ID)
    private Long pk;

    @NotBlank(message = "Описание не может быть пустым или состоять только из пробелов")
    @Size(min = 8, max = 64, message = "Описание должно быть от 8 до 64 символов")
    @ApiModelProperty(value = "Описание объявления", example = OpenApiConstant.DESCRIPTION)
    private String description;

    @ApiModelProperty(value = "Путь к картинке объявления", example = OpenApiConstant.AD_IMAGE)
    private String image;

    @Min(value = 0, message = "Цена не может быть меньше 0")
    @Max(value = 10_000_000, message = "Цена не может превышать 10 000 00")
    @ApiModelProperty(value = "Цена объявления", example = OpenApiConstant.PRICE)
    private Integer price;

    @Size(min = 4, max = 32, message = "Заголовок должен быть от 4 до 32 символов")
    @ApiModelProperty(value = "Заголовок объявления", example = OpenApiConstant.TITLE)
    private String title;
    @NotBlank(message = "Email не может быть пустым или состоять только из пробелов")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email должен быть валидным адресом электронной почты"
    )

    //---------------берем из автора----------------//
    @ApiModelProperty(value = "Логин автора объявления", example = "user@example.com")
    private String email;

    @NotBlank(message = "Имя не может быть пустым или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Имя должно быть от 3 до 10 символов")
    @ApiModelProperty(value = "Имя пользователя", example = OpenApiConstant.NAME)
    private String authorFirstName;

    @NotBlank(message = "Фамилия не может быть пустой или состоять только из пробелов")
    @Size(min = 3, max = 10, message = "Фамилия должна быть от 3 до 10 символов")
    @ApiModelProperty(value = "Фамилия автора объявления", example = OpenApiConstant.LAST_NAME)
    private String authorLastName;

    @NotBlank(message = "Телефон не может быть пустым или состоять только из пробелов")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен быть валидным номером в формате +7 (XXX) XXX-XX-XX"
    )
    @ApiModelProperty(value = "Телефон автора объявления", example = OpenApiConstant.PHONE)
    private String phone;


}
