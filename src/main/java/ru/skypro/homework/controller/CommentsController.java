package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Comment;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * REST-контроллер для управления комментариями.
 * Обрабатывает запросы на создание, обновление, удаление и получение комментариев.
 */
@Slf4j
@RestController
@Tag(name = "Комментарии", description = "API для управления комментариями к объявлениям")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {

    /**
     * Получает список комментариев, связанных с указанным объявлением.
     *
     * @param adId идентификатор объявления, для которого нужно получить комментарии
     * @return список комментариев, связанных с объявлением
     */
    @Operation(
            summary = "Получение комментариев объявления",
            description = "Возвращает список всех комментариев для указанного объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable("adId") int adId) {
        log.info("Получение комментариев для объявления с id {}", id);
        // TODO: Логика в классе сервиса для получения комментариев
        return ResponseEntity.ok(List.of(new Comment()));
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param adId        идентификатор объявления, к которому добавляется комментарий
     * @param commentData данные для создания нового комментария
     * @return созданный комментарий
     */
    @Operation(
            summary = "Добавление комментария к объявлению",
            description = "Позволяет добавить новый комментарий к указанному объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Comment> addComment(
            @PathVariable("adId") int adId,
            @RequestBody CreateOrUpdateCommentDto commentData) {
        log.info("Добавление комментария к объявлению с id {}", id);
        // TODO: Логика в классе сервиса добавления комментария
        return ResponseEntity.ok(new Comment());
    }

    /**
     * Удаляет комментарий по идентификатору.
     *
     * @param adId      идентификатор объявления, к которому относится комментарий
     * @param commentId идентификатор комментария, который нужно удалить
     * @return статус ответа 200 OK при успешном удалении
     */
    @Operation(
            summary = "Удаление комментария",
            description = "Удаляет комментарий по указанным идентификаторам объявления и комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("adId") int adId,
            @PathVariable("commentId") int commentId) {
        log.info("Удаление комментария с id {} для объявления с id {}", commentId, adId);
        // TODO: Логика в классе сервиса (метода) для удаления комментария
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет существующий комментарий.
     *
     * @param adId        идентификатор объявления, к которому относится комментарий
     * @param commentId   идентификатор комментария, который нужно обновить
     * @param commentData данные для обновления комментария
     * @return обновлённый комментарий
     */
    @Operation(
            summary = "Обновление комментария",
            description = "Обновляет текст существующего комментария по указанным идентификаторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("adId") int adId,
            @PathVariable("commentId") int commentId,
            @RequestBody CreateOrUpdateCommentDto commentData) {
        log.info("Обновление комментария с id {} для объявления с id {}", commentId, adId);
        // TODO: Логика в классе сервиса для обновления комментария
        return ResponseEntity.ok(new Comment());
    }
}
