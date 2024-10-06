package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exceptions.InvalidPassword;
import ru.skypro.homework.exceptions.UserNotAuthenticatedException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.UserRepository;

import java.io.IOException;

/**
 * Сервис для работы с пользователями, включает в себя обновление пароля, данных пользователя,
 * а также управление изображениями пользователей.
 *
 * Логирование осуществляется с помощью аннотации {@link Slf4j}.
 * Используются репозитории {@link UserRepository} и сервис {@link ImageService} для работы с изображениями и данными пользователя.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final PasswordEncoder encoder;

    /**
     * Метод обновляет пароль текущего, авторизованного пользователя.
     * <p>Метод получает объект {@code NewPassword}, который содержит два поля со старым и новым паролями.</p>
     * В методе осуществляется у текущего пользователя наличия аутентификации.
     * <p></>Далее по его email находим пользователя в БД для сравнения паролей из параметра и объекта с БД.
     * Если пароли совпали, то сохраняется новый пароль. Переменная (объект user) с новым, измененным паролем
     * сохраняется в БД.</p>
     *
     * @param dto объект для смены пароля {@code NewPassword}
     * @return объект {@link NewPassword} для ответа эндпоинту контроллера
     */
    public NewPassword changePassword(NewPassword dto) {
        log.info("Вызван метод смены пароля для авторизованного пользователя");
        User authotizedUser = getCurrentUser();
        User user = userRepository.findByEmail(authotizedUser.getEmail()).orElseThrow(UserNotFoundException::new);
        if (encoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(dto.getNewPassword()));
        } else {
            log.info("Введен неправильный пароль");
            throw new InvalidPassword("Введен неправильный пароль");
        }
        log.info("Новый пароль успешно сохранен");
        userRepository.save(user);
        return userMapper.toNewPassword(user);
    }

    /**
     * Метод возвращает информацию о текущем, авторизованном пользователе.
     * Метод, используя объект {@link Authentication} через вспомогательный метод {@code getCurrentUser} возвращает
     * аутентифицированного пользователя и , с помощью его данных,
     * находит в БД {@link UserRepository} пользователя с соответствующими данными и возвращает его.
     *
     * @return dto пользователя
     */
    public UserDto getAuthorizedUser() {
        log.info("Вызван метод получения авторизованного пользователя");
        User authenticatedUser = getCurrentUser();
        User user = userRepository.findByEmail(authenticatedUser.getEmail()).orElseThrow(UserNotFoundException::new);
        UserDto userDto = userMapper.toUserDto(user);

        return userDto;
    }

    /**
     * Метод изменяет данные пользователя, а именно имя, фамилию и номер телефона.
     * <p>В начале метод получает логин авторизованного пользователя с помощью метода {@code getCurrentUser()}
     * и записывает его в переменную.</p>
     * <p>По логину находит данные пользователя в БД и кладет их в сущность user.
     * Сущность user заполняется измененными данными из парамера updateUser.</p>
     * <p>В итоге измененный объект user сохраняется в БД, и он же возвращается из метода.</p>
     *
     * @param dto обновленных данных
     * @return объект для вызывающего эндпоинта контроллера для опредления ответа клиенту
     */
    public UpdateUserDto updateUserData(UpdateUserDto dto) {
        log.info("Вызван метод обновления данных авторизованного пользователя");
        User authenticatedUser = getCurrentUser();
        User user = userRepository.findByEmail(authenticatedUser.getEmail()).orElseThrow(UserNotFoundException::new);
        User updatedUser = userMapper.fromUpdatedUserDtoToUser(dto, user);
        userRepository.save(updatedUser);
        return userMapper.toUpdateUserDto(updatedUser);
    }

    /**
     * Вспомогательный метод для получения о возвращения аутентифицированного пользователя
     *
     * @return аутентифицированный пользователь
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("Пользователь не авторизован", UserNotAuthenticatedException.class);
            throw new UserNotAuthenticatedException("Пользователь не авторизован");
        }
        return userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Метод обновляет изображение текущего пользователя.
     *
     * @param image объект {@link MultipartFile}, содержащий загруженное изображение.
     * @throws IOException если произошла ошибка при сохранении изображения.
     * @throws UserNotAuthenticatedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь с таким email не найден.
     */
    public void updateUserImage(MultipartFile image) throws IOException {
        imageService.createImage(getCurrentUser(), image);
    }
}
