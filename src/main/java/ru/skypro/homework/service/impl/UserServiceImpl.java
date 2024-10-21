package ru.skypro.homework.service.impl;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.UploadImage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SecurityServiceImpl securityService;

    /**
     * Метод по обновлению пароля.
     * В качестве аргумента данный метод принимает модель по обновлению пароля, содержащую текущий пароль и новый пароль.
     * Из контекста безопасности получаем логин текущего пользователя, по логину получаем самого пользователя.
     * Если пароль, извлеченный из текущего пользователя, совпадает с паролем, введенным данным пользователем,
     * то обновляем пароль пользователя, получив новый пароль из модели по обновлению пароля (тоже введенный пользователем).
     * @param newPassword - новый пароль
     * @return true, если пароль, введенный пользователем, совпал с паролем текущего пользователя, иначе - false.
     */
    @Override
    @Transactional
    public boolean updatePassword(NewPasswordDto newPassword) {
        String email = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);

        boolean isEqualsPass = user.getPassword().equals(newPassword.getCurrentPassword());
        if (isEqualsPass) {
            userMapper.updateUserPasswordFromDto(newPassword, user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)                           // Метод будет выполняться в транзакции только для чтения
    public UserDto getAuthenticatedUser() {
        String email = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser) {
        String email = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        userMapper.updateUserFromUpdateUserDto(updateUser, user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto updateAuthenticatedUserImage(MultipartFile file) {
        String username = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(username);
        try {
            String urlImage = UploadImage.uploadImage(file);
            user.setImage(urlImage);
            // В сущность User сохраняется путь к файлу, состоящий из "/" и имени файла (без имени папки)
        } catch (IOException e) {
            log.error("Error uploading image file path = {}", user.getImage(), e);
            throw new RuntimeException(e);
        }
        userRepository.save(user);
        return userMapper.toDto(user);
    }

//    @Override
//    @Transactional(isolation = Isolation.REPEATABLE_READ)
//    public boolean saveUser(User user) {
//        return userRepository.save(user);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public boolean emailExists(String email) {
//        return userRepository.existsByEmail(email);
//    }


    @Override
    public UserDetails loadByUserName(String username) {
        User user = getUserByEmailFromDb(username);
        return new UserSecurityDetails(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmailFromDb(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
