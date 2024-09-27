package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

/**
 * REST-контроллер для управления пользователями.
 * Обрабатывает запросы на обновление информации о пользователе, его пароля и аватара.
 */
@Slf4j
@RestController
@Tag(name = "Пользователи", description = "API для управления пользователями")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    /**
     * Обновляет пароль авторизованного пользователя.
     *
     * @param dto карта с новыми значениями пароля, где ключ – это старый пароль и новый пароль
     * @return сообщение о статусе обновления пароля
     */
    @Operation(
            summary = "Обновление пароля",
            description = "Позволяет авторизованному пользователю обновить свой пароль"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@RequestBody NewPassword dto, Authentication authentication) {

        log.info("Обновление пароля для пользователя");
        // TODO: Логика в методе класса сервиса для обновления пароля
        return ResponseEntity.ok("Password updated successfully.");
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @return информация о текущем авторизованном пользователе
     */
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            description = "Возвращает информацию о текущем авторизованном пользователе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser() {
        log.info("Получение информации об авторизованном пользователе");
        // TODO: Логика в методе класса сервиса для получения информации о пользователе
        return ResponseEntity.ok(new UserDto());
    }

    /**
     * Обновляет информацию о текущем авторизованном пользователе.
     *
     * @param updateUser объект с новыми данными пользователя
     * @return обновлённая информация о пользователе
     */
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            description = "Позволяет авторизованному пользователю обновить свои личные данные"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto updateUser) {
        log.info("Обновление информации об авторизованном пользователе");
        // TODO: Логика в методе класса сервиса для обновления информации о пользователе
        return ResponseEntity.ok(updateUser);
    }

    /**
     * Обновляет аватар текущего авторизованного пользователя.
     *
     * @param image файл изображения, который будет установлен как аватар
     * @return сообщение о статусе обновления аватара
     */
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            description = "Позволяет авторизованному пользователю обновить свой аватар"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User image updated successfully",
                    content = @Content(mediaType = "MultipartFile",
                            schema = @Schema(implementation = MultipartFile.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @PatchMapping(value = "/me/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image) {
        log.info("Обновление аватара пользователя");
        // TODO: Логика в методе класса обновления аватара
        return ResponseEntity.ok("User image updated successfully.");
    }
}
