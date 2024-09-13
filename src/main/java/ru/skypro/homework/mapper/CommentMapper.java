package ru.skypro.homework.mapper;

import java.util.List;
import java.util.function.BinaryOperator;
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

    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    CommentDto toDto(Comment comment);

    void toEntityFromCreateUpdatDto(CreateOrUpdateCommentDto dto,
                                    @MappingTarget Comment comment);
}
