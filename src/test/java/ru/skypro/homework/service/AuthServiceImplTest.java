package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;


    private User user;
    private User newUser;
    private RegisterDto registerDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {

        user = ConstantGeneratorFotTest.userGenerator();
        newUser = ConstantGeneratorFotTest.newUserGenerator_1();
        registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        loginDto = ConstantGeneratorFotTest.loginDtoGenerator();
    }

    @Disabled("Тест временно не работает")
    @Test
    void testRegister_Success() {

        //when(userRepository.existsByEmail(registerDto.getUsername())).thenReturn(false);
        when(userService.getUserByEmailFromDb(registerDto.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(anyString());
        when(userMapper.toEntityFromRegisterDto(registerDto)).thenReturn(newUser);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        boolean isRegister = authService.register(registerDto);

        assertTrue(isRegister);

        verify(userRepository, times(1)).existsByEmail(registerDto.getUsername());
        verify(passwordEncoder, times(1)).encode(registerDto.getPassword());
        verify(userRepository, times(1)).save(newUser);
    }

    @Disabled("Тест временно не работает")
    @Test
    void testRegister_ThrowsUserAlreadyRegisteredException() {

        when(userRepository.existsByEmail(registerDto.getUsername()))
                .thenThrow(new UserAlreadyRegisteredException("Пользователь с таким логином уже зарегистрирован"));

        //boolean isRegister = authService.register(registerDto);

        //assertFalse(isRegister);
        assertThrows(UserAlreadyRegisteredException.class, () -> authService.register(registerDto));
        verify(userRepository, times(1)).existsByEmail(registerDto.getUsername());

    }

    @Disabled("Тест временно не работает")
    @Test
    void testLogin_Success() {

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());

        when(userService.getUserByEmailFromDb(loginDto.getUsername())).thenReturn(user);
        when(userDetailsService.loadUserByUsername(loginDto.getUsername())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())).thenReturn(true);

        boolean isLogin = authService.login(loginDto);

        assertTrue(isLogin);
        verify(userService, times(1)).getUserByEmailFromDb(loginDto.getUsername());
        verify(userDetailsService, times(1)).loadUserByUsername(loginDto.getUsername());
        verify(passwordEncoder, times(1)).matches(loginDto.getUsername(), userDetails.getPassword());
    }

}
