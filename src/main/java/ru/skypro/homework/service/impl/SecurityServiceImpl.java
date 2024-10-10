package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис по извлечению из контекста безопасности логина пользователя
 */
@Service
public class SecurityServiceImpl {

    public String getAuthenticatedUserName() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
