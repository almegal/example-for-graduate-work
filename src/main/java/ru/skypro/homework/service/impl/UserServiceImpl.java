package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final Login login;

    public UserServiceImpl(Login login) {
        this.login = login;
    }

    @Override
    public boolean updatePassword(NewPassword newPassword) {
        return false;
    }

    @Override
    public User getAuthenticatedUser() {
        return null;
    }

    @Override
    public User updateAuthenticatedUserInfo(UpdateUser updateUser) {
        return null;
    }

    @Override
    public boolean updateAuthenticatedUserImage(MultipartFile image) {
        return false;
    }

}
