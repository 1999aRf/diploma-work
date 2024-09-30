package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    @Mapping(source = "email", target = "username")
    Login toLoginDto(User user);

    @Mappings({
            @Mapping(source = "username", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
    })

    User toEntity(Register register);

    @Mappings({
            @Mapping(source = "email", target = "username") ,
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
    })
    Register toRegisterDto(User user);


    @Mappings({
            @Mapping(source = "id", target = "id") ,
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "imageUser", target = "image")

    })
    UserDto toUserDto(User user);

    @Mappings({
            @Mapping(source = "id", target = "id") ,
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "image", target = "imageUser")

    })
    User toEntity(UserDto userDto);

    @Mappings({
            @Mapping(source = "password", target = "currentPassword")
    })
    NewPassword toNewPassword(User user);

    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone")

    })
    User fromUpdatedUserDtoToUser(UpdateUserDto dto, @MappingTarget User user);

    UpdateUserDto toUpdateUserDto(User user);
}
