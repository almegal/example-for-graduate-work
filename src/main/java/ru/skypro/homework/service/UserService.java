package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.util.UploadImage;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.exception.NotFoundException;

public interface UserService {

    /**
     * Обновляет пароль пользователя.
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link UserMapper#updateUserPasswordFromDto(NewPasswordDto, User)}
     * @param newPassword - модель по обновлению пароля {@link NewPasswordDto}
     * @return true, если пароль, введенный пользователем, совпал с паролем текущего пользователя, иначе - false.
     */
    boolean updatePassword(NewPasswordDto newPassword);

    /**
     * Извлекает текущего аутентифицированного пользователя.
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link UserMapper#toDto(User)}
     * @return - объект UserDTO, представляющий текущего пользователя.
     */
    UserDto getAuthenticatedUser();

    /**
     * Изменение данных аутентифицированного пользователя.
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link UserMapper#updateUserFromUpdateUserDto(UpdateUserDto, User)}
     * {@link UserMapper#toDto(User)}
     * @param updateUser - модель для обновления информации о пользователе
     * @return - объект UserDto, представляющий модель пользователя с обновленными данными
     */
    UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser);

    /**
     * Обновление аватарки аутентифицированного пользователя.
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link UploadImage#uploadImage(MultipartFile)}
     * {@link UserRepository#save(Object)}
     * {@link UserMapper#toDto(User)}
     * @param file - загружаемая в файловую систему картинка для пользователя
     * @return AdDto - модель объявления с путем к обновленной аватарке
     */
    UserDto updateAuthenticatedUserImage(MultipartFile file);

//    /**
//     * Сохранение нового пользователя в базу данных.
//     * Метод использует:
//     * {@link UserRepository#existsByEmail(String)}
//     * {@link UserMapper#toEntityFromRegisterDto(RegisterDto)}
//     * {@link UserRepository#save(Object)}
//     * @param user - новый пользователь
//     * @return true, если регистрация нового пользователя прошла успешно
//     */
//    boolean saveUser(User user);
//
//    boolean emailExists(String email);

    /**
     * Этот метод переопределен из интерфейса UserDetailsManager
     * для настройки процесса аутентификации для Spring Security.
     * Он извлекает данные пользователя, включая имя пользователя, пароль и роль, для создания объекта UserDetails.
     * Метод использует {@link UserService#getUserByEmailFromDb(String)} 
     * @param username - логин пользователя
     * @return - объект UserDetails, содержащий сведения о пользователе
     * @throws NotFoundException - исключение, если пользователь по введенному логину не найден в базе данных
     */
    UserDetails loadByUserName(String username);

    /**
     * Получение из базы данных пользователя по введенному логину
     * Метод использует {@link UserRepository#findByEmail(String)}
     * @param email - логин пользователя
     * @return объект класса User - пользователя
     */
    User getUserByEmailFromDb(String email);


}
