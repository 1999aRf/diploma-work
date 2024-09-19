package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.util.List;

/**
 * REST-контроллер для управления комментариями.
 * Обрабатывает запросы на создание, обновление, удаление и получение комментариев.
 */
@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {

    /**
     * Получает список комментариев, связанных с указанным объявлением.
     *
     * @param adId идентификатор объявления, для которого нужно получить комментарии
     * @return список комментариев, связанных с объявлением
     */
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable("adId") int adId) {
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
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping
    public ResponseEntity<Comment> addComment(
            @PathVariable("adId") int adId,
            @RequestBody CreateOrUpdateComment commentData) {
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
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("adId") int adId,
            @PathVariable("commentId") int commentId) {
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
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("adId") int adId,
            @PathVariable("commentId") int commentId,
            @RequestBody CreateOrUpdateComment commentData) {
        // TODO: Логика в классе сервиса для обновления комментария
        return ResponseEntity.ok(new Comment());
    }
}
