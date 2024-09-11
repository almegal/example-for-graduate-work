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
    AdDto toDto(Ad ad);

    List<AdDto> toDtos(List<Ad> ads);

    ExtendedAdDto toExtendedDto(Ad ad);

    void updateAdFromUpdateAdDto(CreateOrUpdateAdDto dto, @MappingTarget Ad ad);
}
