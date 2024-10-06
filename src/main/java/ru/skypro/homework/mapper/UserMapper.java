package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,
imports = {ru.skypro.homework.dto.Role.class})
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
            @Mapping(source = "username", target = "email") ,
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
    })
    User fromRegisterDto(Register register);


    @Mappings({
            @Mapping(source = "id", target = "id") ,
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "imageUser.filePath", target = "image")

    })
    UserDto toUserDto(User user);

    @Mappings({
            @Mapping(source = "id", target = "id") ,
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "image", target = "imageUser.filePath")

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
