package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Comment;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {



    Comment fromCreateOrUpdateCommentDto(CreateOrUpdateCommentDto dto);
    CreateOrUpdateCommentDto toCreateOrUpdateCommentDto(Comment comment);
    @Mappings({
            @Mapping(source = "author",target = "user.id"),
            @Mapping(source = "authorImage",target = "user.imageUser",ignore =true),
            @Mapping(source = "authorFirstName",target = "user.firstName"),
            @Mapping(source = "createdAt",target = "createdAt"),
            @Mapping(source = "pk",target = "id"),
            @Mapping(source = "text",target = "text")

    })
    Comment fromCommentDto(CommentDto dto);

    @Mappings({
            @Mapping(source = "user.id",target = "author"),
            @Mapping(source = "user.imageUser",target = "authorImage",ignore =true),
            @Mapping(source = "user.firstName",target = "authorFirstName"),
            @Mapping(source = "createdAt",target = "createdAt"),
            @Mapping(source = "id",target = "pk"),
            @Mapping(source = "text",target = "text")

    })
    CommentDto toCommentDto(Comment comment);
}
