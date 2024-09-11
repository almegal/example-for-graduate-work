package ru.skypro.homework.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment toEntity(CreateOrUpdateCommentDto dto);

    List<CommentDto> toDtos(List<Comment> comments);

    @Mapping(source = "id", target = "pk")
    CommentDto toDto(Comment comment);

    void toEntityFromCreateUpdatDto(CreateOrUpdateCommentDto dto,
                                    @MappingTarget Comment comment);
}
