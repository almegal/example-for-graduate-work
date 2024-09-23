package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

public interface UserService {

    boolean updatePassword(NewPasswordDto newPassword);

    UserDto getAuthenticatedUser();

    UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser);

    boolean updateAuthenticatedUserImage(MultipartFile image);

    boolean saveUser(RegisterDto dto);

    boolean emailExists(String email);

    User getUserByEmailFromDb(String email);

    UserDetails loadByUserName(String username);
}
