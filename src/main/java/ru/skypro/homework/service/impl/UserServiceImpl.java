package ru.skypro.homework.service.impl;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean updatePassword(NewPasswordDto newPassword) {
        String email = getAuthentifacatedUserName();
        User user = getUserByEmailFromDb(email);

        boolean isEqualsPass = user.getPassword().equals(newPassword.getCurrentPassword());

        if (isEqualsPass) {
            userMapper.updateUserPasswordFromDto(newPassword, user);
            return true;
        }
        return false;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserDto getAuthenticatedUser() {
        String email = getAuthentifacatedUserName();
        User user = getUserByEmailFromDb(email);
        return userMapper.toDto(user);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser) {
        String email = getAuthentifacatedUserName();
        User user = getUserByEmailFromDb(email);
        userMapper.updateUserFromUpdateUserDto(updateUser, user);
        return userMapper.toDto(user);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean updateAuthenticatedUserImage(MultipartFile image) {
        try {
            byte[] img = image.getBytes();
            User user = getUserByEmailFromDb(getAuthentifacatedUserName());
            user.setAvatar(img);
            return true;
        } catch (IOException ex) {
            log.error("Ошибка при загрузке изображения: {}", ex.getMessage());
        }
        return false;
    }


    @Override
    public boolean saveUser(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = userMapper.toEntityFromRegisterDto(dto);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private String getAuthentifacatedUserName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private User getUserByEmailFromDb(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
