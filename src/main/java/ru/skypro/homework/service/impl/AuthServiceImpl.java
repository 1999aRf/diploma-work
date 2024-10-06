package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exceptions.InvalidPassword;
import ru.skypro.homework.exceptions.UserAlreadyExistsException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.net.http.HttpRequest;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;
    private final UserRepository repository;



    @Override
    public boolean login(String userName, String password) {
        log.info("Запущен метод login() сервиса {}",this.getClass());
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new InvalidPassword("Неверный пароль");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean register(Register register) {
        log.info("Запущен метод register() сервиса {}", this.getClass());
        if (manager.loadUserByUsername(register.getUsername()) != null) {
            throw new UserAlreadyExistsException("Пользователь уже существует");
        }
        User fromDto = mapper.fromRegisterDto(register);
        fromDto.setPassword(encoder.encode(fromDto.getPassword()));
        repository.save(fromDto);
        return true;
    }

}
