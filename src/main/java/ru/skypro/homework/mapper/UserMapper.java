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

    @Mappings(value = {
            @Mapping(target = "id", constant = "0"),
            @Mapping(target = "email", expression = "java(userDetails.getUsername())"),
            @Mapping(target = "firstName", constant = "firstName"),
            @Mapping(target = "lastName", constant = "lastName"),
            @Mapping(target = "phone", constant = "+7 (111) 111-11-11"),
            @Mapping(target = "role", expression = "java(Role.USER)"),
            @Mapping(target = "imageUser", ignore = true)
    })
    User userDetailsToUser(UserDetails userDetails);

    UpdateUserDto toUpdateUserDto(User user);
}
