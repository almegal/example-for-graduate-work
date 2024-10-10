package ru.skypro.homework.service;

import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис по аутентификации и регистрации пользователя
 */
public interface AuthService {

    /**
     * Логика по аутентификации пользователя.
     * Метод использует:
     * {@link org.springframework.security.core.userdetails.UserDetailsService}
     * {@link org.springframework.security.crypto.password.PasswordEncoder#matches(CharSequence, String)} 
     * @param loginDto - модель пользователя {@link LoginDto}
     * @return true, если аутентификация прошла успешно, иначе - будет выброшено соответствующее исключение
     */
    boolean login(LoginDto loginDto);

    /**
     * Логика по регистрации нового пользователя.
     * Метод использует:
     * {@link UserRepository#existsByEmail(String)}
     * {@link org.springframework.security.crypto.password.PasswordEncoder#encode(CharSequence)} 
     * {@link ru.skypro.homework.mapper.UserMapper#toEntityFromRegisterDto(RegisterDto)}
     * {@link UserRepository#save(Object)}
     * @param registerDto - модель для регистрации нового пользователя {@link RegisterDto}
     * @return true, если регистрация прошла успешно, иначе - будет выброшено соответствующее исключение
     */
    boolean register(RegisterDto registerDto);
}
