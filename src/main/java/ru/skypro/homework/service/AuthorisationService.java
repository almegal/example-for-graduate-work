package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.User;

public interface AuthorisationService {
     default User authenticate(Login authenticationLogin) {
        return null;
    }
}
