package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.service.NewPasswordService;

import java.util.List;
@Service
public class NewPasswordServiceImpl implements NewPasswordService {
    @Override
    public boolean createNewPassword(NewPassword newPassword) {
        return false;
    }

    @Override
    public List<NewPassword> getNewPasswords() {
        return null;
    }

    @Override
    public NewPassword getNewPasswordById(Long id) {
        return null;
    }

    @Override
    public boolean updateNewPassword(Long id, NewPassword newPassword) {
        return false;
    }

    @Override
    public boolean deleteNewPassword(Long id) {
        return false;
    }
}
