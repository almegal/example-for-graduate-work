package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "CreateOrUpdateComment", description = "Модель для создания или обновления комментария")
public class CreateOrUpdateCommentDto {

    @NotBlank
    @Size(min = 8, max = 64)
    @ApiModelProperty(value = "Текст комментария", example = OpenApiConstant.TEXT)
    private String text;

    @JsonCreator
    public CreateOrUpdateCommentDto(@JsonProperty("text") String text) {
        this.text = text;
    }
}
