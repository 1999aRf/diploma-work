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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.RecievedAdDto;
import ru.skypro.homework.dto.UpdateAdDto;
import ru.skypro.homework.model.Ad;

import java.util.List;

/**
 * REST-контроллер для управления объявлениями.
 * Обрабатывает запросы на создание, обновление, удаление и получение объявлений.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
@Tag(name = "Обьявления")
public class AdsController {

    /**
     * Получение всех объявлений.
     *
     * @return список всех объявлений.
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecievedAdDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<RecievedAdDto>> getAllAds() {
        // TODO: Дополнить логику получения всех объявлений в сервисе получения всех объявлений
        return ResponseEntity.ok(List.of(new RecievedAdDto()));
    }

    /**
     * Добавление нового объявления.
     *
     * @param image        изображение для объявления.
     * @param adProperties свойства нового объявления.
     * @return созданное объявление.
     */
    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(
            @RequestParam("image") MultipartFile image,
            @RequestParam("properties") AdDto adProperties) {
        // TODO: Дополнить логику добавления объявления в сервисе
        return ResponseEntity.status(201).body(new AdDto());
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return расширенная информация о выбранном объявлении.
     */
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateAdDto> updateAd(
            @PathVariable("id") int id,
            @RequestBody UpdateAdDto updateData) {
        // TODO: Дополнить логику обновления объявления по значению id объявления
        return ResponseEntity.ok(new UpdateAdDto());
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return список объявлений текущего пользователя.
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecievedAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<RecievedAdDto> getMyAds() {
        // TODO: Дополнить логику получения объявлений авторизованного пользователя
        return ResponseEntity.ok(new RecievedAdDto());
    }

    /**
     * Обновление картинки для объявления по его идентификатору.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение для объявления.
     * @return сообщение об успешном обновлении изображения.
     */
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PatchMapping(value = "/{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateImage(
            @PathVariable("id") int id,
            @RequestParam("image") MultipartFile image) {
        // TODO: Дополнить логику обновления картинки объявления по id объявления
        return ResponseEntity.ok("Image updated successfully.");
    }
}
