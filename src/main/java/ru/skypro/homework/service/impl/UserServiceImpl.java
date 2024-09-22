package ru.skypro.homework.service.impl;

import static ru.skypro.homework.util.UploadImage.uploadImage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean updatePassword(NewPasswordDto newPassword) {
        String email = getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);

        boolean isEqualsPass = user.getPassword()
                .equals(newPassword.getCurrentPassword());

        if (isEqualsPass) {
            userMapper.updateUserPasswordFromDto(newPassword, user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getAuthenticatedUser() {
        String email = getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser) {
        String email = getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        userMapper.updateUserFromUpdateUserDto(updateUser, user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateAuthenticatedUserImage(MultipartFile image) {
        User user = getUserByEmailFromDb(getAuthenticatedUserName());
        try {
            user.setImage(uploadImage(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean saveUser(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = userMapper.toEntityFromRegisterDto(dto);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmailFromDb(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private String getAuthenticatedUserName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
