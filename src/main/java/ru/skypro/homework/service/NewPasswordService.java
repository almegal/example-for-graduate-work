package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;

import java.util.List;

public interface NewPasswordService {
    boolean createNewPassword(NewPassword newPassword);

    List<NewPassword> getNewPasswords();

    NewPassword getNewPasswordById(Long id);

    boolean updateNewPassword(Long id, NewPassword newPassword);

    boolean deleteNewPassword(Long id);
}
