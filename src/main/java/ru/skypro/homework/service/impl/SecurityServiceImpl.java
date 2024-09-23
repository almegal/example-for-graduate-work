package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl {
    public String getAuthenticatedUserName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
