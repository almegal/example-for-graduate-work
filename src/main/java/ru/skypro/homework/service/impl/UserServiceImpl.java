package ru.skypro.homework.service.impl;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public boolean createUser(User user) {
        return false;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public boolean updateUser(Long id, User user) {
        return false;
    }

    @Override
    public boolean deleteUser(Long id) {
        return false;
    }
}
