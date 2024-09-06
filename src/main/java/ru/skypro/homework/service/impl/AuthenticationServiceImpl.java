package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.AuthorisationService;
@Service
public class AuthenticationServiceImpl implements AuthorisationService {
    @Override
    public User authenticate(Login authenticationLogin) {
        return null;
    }
}
