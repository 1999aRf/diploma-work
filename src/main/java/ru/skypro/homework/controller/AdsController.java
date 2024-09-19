package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;

import java.util.List;

/**
 * REST-контроллер для управления объявлениями.
 * Обрабатывает запросы на создание, обновление, удаление и получение объявлений.
 */
@RestController
@RequestMapping("/ads")
public class AdsController {

    /**
     * Получение всех объявлений.
     *
     * @return список всех объявлений.
     */
    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public ResponseEntity<List<Ad>> getAllAds() {
        // TODO: Дополнить логику получения всех объявлений в сервисе получения всех объявлений
        return ResponseEntity.ok(List.of(new Ad()));
    }

    /**
     * Добавление нового объявления.
     *
     * @param image        изображение для объявления.
     * @param adProperties свойства нового объявления.
     * @return созданное объявление.
     */
    @Operation(summary = "Добавление объявления")
    @PostMapping
    public ResponseEntity<Ad> addAd(
            @RequestParam("image") MultipartFile image,
            @RequestParam("properties") CreateOrUpdateAd adProperties) {
        // TODO: Дополнить логику добавления объявления в сервисе
        return ResponseEntity.status(201).body(new Ad());
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return расширенная информация о выбранном объявлении.
     */
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable("id") int id) {
        // TODO: Дополнить логику получения объявления по значению id объявления
        return ResponseEntity.ok(new ExtendedAd());
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return пустой ответ с кодом 204.
     */
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") int id) {
        // TODO: Дополнить логику удаления объявления по значению идентификатора
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновление объявления по его идентификатору.
     *
     * @param id         идентификатор объявления.
     * @param updateData обновленные данные объявления.
     * @return обновленное объявление.
     */
    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(
            @PathVariable("id") int id,
            @RequestBody CreateOrUpdateAd updateData) {
        // TODO: Дополнить логику обновления объявления по значению id объявления
        return ResponseEntity.ok(new Ad());
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return список объявлений текущего пользователя.
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getMyAds() {
        // TODO: Дополнить логику получения объявлений авторизованного пользователя
        return ResponseEntity.ok(List.of(new Ad()));
    }

    /**
     * Обновление картинки для объявления по его идентификатору.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение для объявления.
     * @return сообщение об успешном обновлении изображения.
     */
    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping("/{id}/image")
    public ResponseEntity<String> updateImage(
            @PathVariable("id") int id,
            @RequestParam("image") MultipartFile image) {
        // TODO: Дополнить логику обновления картинки объявления по id объявления
        return ResponseEntity.ok("Image updated successfully.");
    }
}
