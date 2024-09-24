package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Override
    public boolean login(LoginDto loginDto) {
        UserDetails userDetails = manager.loadUserByUsername(loginDto.getUsername());
        return encoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        if (userService.emailExists(registerDto.getUsername())) {
            return false;
        }
        registerDto
                .setPassword(encoder.encode(registerDto.getPassword()));
        return userService.saveUser(registerDto);
    }
}

