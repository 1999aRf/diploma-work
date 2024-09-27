package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exceptions.InvalidCurrentPassword;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public NewPassword changePassword(NewPassword dto, String login) {

        User user = userRepository.findByEmail(login).orElseThrow(UserNotFoundException::new);

        if (user.getPassword().equals(dto.getCurrentPassword())) {
            throw new InvalidCurrentPassword("Введен неправильный пароль");
        } else {
            user.setPassword(dto.getCurrentPassword());
        }
        userRepository.save(user);
        return userMapper.toNewPassword(user);
    }

    public UserDto getAuthorizedUser(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow(UserNotFoundException::new);
        return userMapper.toUserDto(user);
    }

    public UpdateUserDto updateUserData(UpdateUserDto dto, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow(UserNotFoundException::new);
        User updatedUser = userMapper.fromUpdatedUserDtoToUser(dto, user);
        userRepository.save(updatedUser);
        return userMapper.toUpdateUserDto(updatedUser);
    }


}
