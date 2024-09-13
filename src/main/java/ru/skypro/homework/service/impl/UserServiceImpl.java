package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Repository.UserRepository;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public boolean updatePassword(NewPasswordDto newPassword) {
        return false;
    }

    @Override
    public UserDto getAuthenticatedUser() {
        return null;
    }

    @Override
    public UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser) {
        return null;
    }

    @Override
    public boolean updateAuthenticatedUserImage(MultipartFile image) {
        return false;
    }

}
