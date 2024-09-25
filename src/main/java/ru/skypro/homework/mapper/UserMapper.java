package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

@Mapper
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
}
