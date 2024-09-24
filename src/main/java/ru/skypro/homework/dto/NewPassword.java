package ru.skypro.homework.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPassword {

    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String currentPassword;
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String newPassword;
}
