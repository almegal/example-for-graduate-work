package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;

import java.util.List;

public interface UserService {
    boolean createUser(User user);

    List<User> getUsers();

    User getUserById(Long id);

    boolean updateUser(Long id, User user);

    boolean deleteUser(Long id);
}
