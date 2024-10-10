package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

/**
 * Класс по аутентификации и регистрации пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Метод по аутентификации пользователя.
     * Получаем из контекста безопасности объект UserDetails по введенному пользователем логину.
     * Если введенный пользователем логин неправильный, то будет выброшено соответствующее исключение.
     * Если введенный пользователем пароль не совпадет с паролем, извлеченным из объекта
     * UserDetails, то будет выброшено исключение о неправильно введенном пароле
     */
    @Override
    public boolean login(LoginDto loginDto) {
        if (userService.getUserByEmailFromDb(loginDto.getUsername()) == null) {
            log.error("Введен неправильный логин = {}", loginDto.getUsername());
            throw new UnauthorizedException("Введен неправильный логин");
        }
        UserDetails userDetails = manager.loadUserByUsername(loginDto.getUsername());
        if (!encoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            log.error("Введен неправильный пароль = {}", loginDto.getPassword());
            throw new UnauthorizedException("Введен неправильный пароль");
        }
        log.info("Пользователь авторизован");
        return encoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }


    /**
     * Метод по регистрации пользователя.
     * Осуществляем в базе данных поиск пользователя по введенному им логину.
     * Если в базе данных уже зарегистрирован пользователь с таким логином,
     * то будет выброшено соответствующее исключение.
     * Передаем в параметры метода encode() пароль, извлеченный из модели для регистрации пользователя,
     * и уже закодированный пароль сохраняем в модель для регистрации пользователя.
     * Через маппер сохраняем логин и пароль пользователя в объект User, а User сохраняем в базу данных.
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getUsername())) {
            log.info("Пользователь с таким логином = {} уже зарегистрирован", registerDto.getUsername());
            throw new UserAlreadyRegisteredException("Пользователь с таким логином уже зарегистрирован");
        }
        registerDto.setPassword(encoder.encode(registerDto.getPassword()));
        User user = userMapper.toEntityFromRegisterDto(registerDto);
        userRepository.save(user);
        log.info("Регистрация прошла успешно! Логин пользователя {}", registerDto.getUsername());
        return true;
    }
}

