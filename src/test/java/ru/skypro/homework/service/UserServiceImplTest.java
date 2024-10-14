package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityServiceImpl securityService;

    @InjectMocks
    private UserServiceImpl service;


    private User user;
    private User userAdmin;
    private NewPasswordDto newPasswordDto;


    @BeforeEach
    void setUp() {
        user = ConstantGeneratorFotTest.userGenerator();
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        newPasswordDto = ConstantGeneratorFotTest.newPasswordDtoGenerator();
    }


//    @Test
//    void updatePassword_Success() {
//
//        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
//        when(userMapper.updateUserPasswordFromDto(any(NewPasswordDto.class), any(User.class))).thenReturn()
//        userMapper.updateUserPasswordFromDto(newPassword, user);
//
//
//
//        when(repository.findByEmail(anyString())).thenReturn(mockUser);
//
//        service.updatePassword(mockPasswordDTO);
//
//        verify(repository).save(any());
//    }


}
