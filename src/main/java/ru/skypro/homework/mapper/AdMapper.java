package ru.skypro.homework.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(target = "image", expression = "java(\"/image/\" + ad.getImageUrl())")
    AdDto toDto(Ad ad);

    List<AdDto> toDtos(List<Ad> ads);

    @Mapping(source = "id", target = "pk")
    @Mapping(target = "image", expression = "java(\"/image/\" + ad.getImageUrl())")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAdDto toExtendedDto(Ad ad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    void updateAdFromUpdateAdDto(CreateOrUpdateAdDto dto, @MappingTarget Ad ad);
}
