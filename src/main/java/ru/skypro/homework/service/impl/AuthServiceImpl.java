package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exceptions.InvalidPassword;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация интерфейса {@link AuthService} для аутентификации и регистрации пользователей.
 *
 * Класс использует сервис {@link UserDetailsService} для загрузки данных пользователя,
 * {@link PasswordEncoder} для хеширования паролей, {@link UserMapper} для преобразования DTO
 * и {@link UserRepository} для взаимодействия с базой данных пользователей.
 *
 * Логирование осуществляется с помощью аннотации {@link Slf4j}.
 */
@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param userName имя пользователя.
     * @param password пароль пользователя.
     * @return {@code true}, если аутентификация прошла успешно.
     * @throws InvalidPassword если введен неверный пароль.
     */
    @Override
    public boolean login(String userName, String password) {
        log.info("Запущен метод login() сервиса {}", this.getClass());
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new InvalidPassword("Неверный пароль");
        }
        return true;
    }

    /**
     * Выполняет регистрацию нового пользователя в системе.
     *
     * @param register объект {@link Register}, содержащий данные для регистрации.
     * @return {@code true}, если регистрация прошла успешно.
     */
    @Override
    public boolean register(Register register) {
        log.info("Запущен метод register() сервиса {}", this.getClass());
        User fromDto = mapper.fromRegisterDto(register);
        fromDto.setPassword(encoder.encode(fromDto.getPassword()));
        repository.save(fromDto);
        return true;
    }
}
