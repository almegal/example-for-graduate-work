package ru.skypro.homework.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.skypro.homework.ConstantGeneratorFotTest.NEW_USER_FIRST_NAME;
import static ru.skypro.homework.ConstantGeneratorFotTest.NEW_USER_LAST_NAME;
import static ru.skypro.homework.ConstantGeneratorFotTest.NEW_USER_PHONE;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_EMAIL;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_FIRST_NAME;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_ID;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_IMAGE;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_LAST_NAME;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_PHONE;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_ROLE;
import static ru.skypro.homework.ConstantGeneratorFotTest.registerDtoGenerator;
import static ru.skypro.homework.ConstantGeneratorFotTest.updateUserDtoGenerator;
import static ru.skypro.homework.ConstantGeneratorFotTest.userDtoGenerator;
import static ru.skypro.homework.ConstantGeneratorFotTest.userGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

// toDo Переписать через параметарайзет тест (argsProvider? или просто сурс)
// toDo задача команде на тесты маппинга по анологии
public class UserMapperTest {
    static final User USER_ENTITY = userGenerator();
    static final UserDto USER_DTO = userDtoGenerator();
    static final UpdateUserDto UPDATE_USER_DTO = updateUserDtoGenerator();
    static final RegisterDto REGISTER_DTO = registerDtoGenerator();
    final UserMapper mapper = UserMapper.INSTANCE;


    @Test
    @DisplayName("Корректно маппится из User в UserDto")
    public void shouldCorrectConvertFromEntityUserToDtoWithCorrectValue() {
        UserDto expexted = UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .phone(USER_PHONE)
                .image(USER_IMAGE)
                .build();

        UserDto actual = mapper.toDto(USER_ENTITY);
        assertNotNull(actual);
        assertEquals(expexted.getId(), actual.getId());
        assertEquals(expexted.getEmail(), actual.getEmail());
        assertEquals(expexted.getLastName(), actual.getLastName());
        assertEquals(expexted.getFirstName(), actual.getFirstName());
        // расскоментировать после уточнения и рефакторинга по полю image
//        assertEquals(expexted.getImage(), actual.getImage());
        assertEquals((expexted.getPhone()), actual.getPhone());
        assertEquals(expexted.getRole(), actual.getRole());
    }

    @Test
    @DisplayName("Корректно маппится из UserDtо в User")
    public void shouldCorrectConvertFromUserDtoToEntityWithCorrectValue() {
        User expected = new User();
        expected.setId(USER_ID);
        expected.setEmail(USER_EMAIL);
        expected.setRole(USER_ROLE);
        expected.setFirstName(USER_FIRST_NAME);
        expected.setLastName(USER_LAST_NAME);
        expected.setPhone(USER_PHONE);
        expected.setImage(USER_IMAGE);

        User actual = mapper.toEntity(USER_DTO);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        // расскоментировать после уточнения и рефакторинга по полю image
//        assertEquals(expected.getImage(), actual.getImage());
        assertEquals((expected.getPhone()), actual.getPhone());
        assertEquals(expected.getRole(), actual.getRole());
    }

    @Test
    @DisplayName("Корректно обновляет данные из UpdateUserDto в User")
    public void shouldCorrectMapFromUpdateUserDtoToUser() {
        User expected = userGenerator();
        expected.setFirstName(NEW_USER_FIRST_NAME);
        expected.setLastName(NEW_USER_LAST_NAME);
        expected.setPhone(NEW_USER_PHONE);

        User actual = userGenerator();
        mapper.updateUserFromUpdateUserDto(UPDATE_USER_DTO, actual);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        // расскоментировать после уточнения и рефакторинга по полю image
//        assertEquals(expected.getImage(), actual.getImage());
        assertEquals((expected.getPhone()), actual.getPhone());
        assertEquals(expected.getRole(), actual.getRole());
    }

    @Test
    @DisplayName("Корректно обновляет данные из RegisterDto в User")
    public void shouldCorrectMapFromRegisterDtoToUser() {
        User expected = new User();
        expected.setEmail(REGISTER_DTO.getUsername());
        expected.setFirstName(REGISTER_DTO.getFirstName());
        expected.setLastName(REGISTER_DTO.getLastName());
        expected.setPassword(REGISTER_DTO.getPassword());
        expected.setPhone(REGISTER_DTO.getPhone());
        expected.setRole(REGISTER_DTO.getRole());

        User actual = mapper.toEntityFromRegisterDto(REGISTER_DTO);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        // расскоментировать после уточнения и рефакторинга по полю image
//        assertEquals(expected.getImage(), actual.getImage());
        assertEquals((expected.getPhone()), actual.getPhone());
        assertEquals(expected.getRole(), actual.getRole());
    }


}
