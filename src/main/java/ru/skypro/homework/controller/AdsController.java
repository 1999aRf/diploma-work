package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.service.AdsService;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final AdsService adsService;

    /**
     * Получение всех объявлений.
     *
     * @return список всех объявлений.
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdsDto.class)))
    })
    @GetMapping()
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
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
    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AdDto> addAd(
            @RequestPart("image") MultipartFile image,
            @RequestPart("properties") CreateOrUpdateAdDto adProperties,
            Authentication authentication) throws IOException {

        return ResponseEntity.status(201).body(adsService.createAd(adProperties,image,authentication));
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
    @PreAuthorize("hasRole('USER') and " + // Разрешен вызов эндпоинта авторизованному пользователю,
            "@adsService.isAdBelongsThisUser(authentication.principal.username,#id) or" +  // если это объявление пренадлежит ему
            "hasRole('ADMIN')") // Разрешен вызов эндпоинта Админу

    public ResponseEntity<ExtendedAd> getAdById(@PathVariable("id") Integer id) {
        ExtendedAd ad = adsService.getAdById(id);
        if (ad == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(ad);
        }
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
    @PreAuthorize("hasRole('ADMIN') or " + // Разрешен вызов эндпоинта авторизованному пользователю,
            "@adsService.isAdBelongsThisUser(authentication.principal.username,#id)" )  // если это объявление пренадлежит ему
            // Разрешен вызов эндпоинта Админу
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id) {
        adsService.deleteAd(id);

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
                            schema = @Schema(implementation = CreateOrUpdateAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or " + // Разрешен вызов эндпоинта авторизованному пользователю,
            "@adsService.isAdBelongsThisUser(authentication.principal.username,#id)"  // если это объявление пренадлежит ему
            ) // Разрешен вызов эндпоинта Админу
    public ResponseEntity<AdDto> updateAd(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateAdDto updateData) {
        AdDto ad = adsService.updateAd(id, updateData);
        return ResponseEntity.ok(ad);
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
                            schema = @Schema(implementation = AdsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') and " + // Разрешен вызов эндпоинта авторизованному пользователю,
            "@adsService.isAdBelongsThisUser(authentication.principal.username,#id) or")
    // если это объявление пренадлежит ему
    public ResponseEntity<AdsDto> getMyAds() {
        return ResponseEntity.ok(adsService.getMyAds());
    }


}
