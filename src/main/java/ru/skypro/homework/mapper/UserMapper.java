package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "image", expression = "java(user.getImage())")
    UserDto toDto(User user);

    User toEntity(UserDto dto);

    void updateUserFromUpdateUserDto(UpdateUserDto dto, @MappingTarget User user);

    @Mapping(source = "newPassword", target = "password")
    void updateUserPasswordFromDto(NewPasswordDto dto, @MappingTarget User user);

    @Mapping(source = "username", target = "email")
    User toEntityFromRegisterDto(RegisterDto dto);

}
