package ru.skypro.homework.service;

import ru.skypro.homework.dto.UpdateUser;

import java.util.List;

public interface UpdateUserService {

    boolean createUpdateUser(UpdateUser updateUser);

    List<UpdateUser> getUpdateUsers();

    UpdateUser getUpdateUserById(Long id);

    boolean updateUser(Long id, UpdateUser updateUser);

    boolean deleteUser(Long id);
}
