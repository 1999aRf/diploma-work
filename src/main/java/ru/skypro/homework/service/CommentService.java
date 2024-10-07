package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Сервис для управления комментариями, связанными с объявлениями.
 * <p>
 * Класс предоставляет функционал для создания, обновления, удаления и получения комментариев.
 * Также поддерживает проверку принадлежности комментария пользователю.
 * <p>
 * Логирование осуществляется с помощью аннотации {@link Slf4j}.
 * Используются репозитории {@link CommentRepository}, {@link AdRepository} и {@link UserRepository},
 * а также маппер {@link CommentMapper} для преобразования между сущностями и DTO.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    /**
     * Получает все комментарии из базы данных.
     *
     * @return список комментариев в виде объектов {@link CommentDto}.
     */
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает комментарий по его идентификатору.
     *
     * @param id идентификатор комментария.
     * @return объект {@link CommentDto} с данными комментария.
     * @throws NoSuchElementException если комментарий не найден.
     */
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
        return commentMapper.toCommentDto(comment);
    }

    /**
     * Создаёт новый комментарий для объявления.
     *
     * @param adId идентификатор объявления, к которому относится комментарий.
     * @param commentDto данные для создания комментария.
     * @return созданный комментарий в виде объекта {@link CommentDto}.
     */
    public CommentDto createComment(Integer adId, CreateOrUpdateCommentDto commentDto) {
        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);

        Comment comment = commentMapper.fromCreateOrUpdateCommentDto(commentDto);
        comment.setUser(getCurrentUser()); // Устанавливаем текущего пользователя как автора
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    /**
     * Обновляет существующий комментарий.
     *
     * @param id идентификатор комментария, который необходимо обновить.
     * @param commentDto данные для обновления комментария.
     * @return обновлённый комментарий в виде объекта {@link CommentDto}.
     * @throws NoSuchElementException если комментарий не найден.
     */
    public CommentDto updateComment(Long id, CreateOrUpdateCommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        commentMapper.updateCommentFromDto(commentDto, comment);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param id идентификатор комментария.
     * @throws NoSuchElementException если комментарий не найден.
     */
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        commentRepository.delete(comment);
    }

    /**
     * Получает все комментарии для указанного объявления.
     *
     * @param id идентификатор объявления.
     * @return объект {@link CommentsDto}, содержащий список комментариев.
     * @throws AdNotFoundException если объявление не найдено.
     * @throws CommentNotFoundException если комментарии для объявления не найдены.
     */
    public CommentsDto getComments(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        List<Comment> commentList = commentRepository.findCommentByAd(ad).orElseThrow(CommentNotFoundException::new);
        List<CommentDto> dtoList = commentList.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
        return new CommentsDto(dtoList.size(), dtoList);
    }

    /**
     * Возвращает текущего аутентифицированного пользователя.
     *
     * @return объект {@link User}, представляющий текущего пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     */

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Проверяет, принадлежит ли комментарий текущему аутентифицированному пользователю.
     *
     * @param nameOfAuthenticatedUser имя текущего аутентифицированного пользователя.
     * @param adId идентификатор объявления, к которому относится комментарий.
     * @param commentId идентификатор комментария.
     * @return {@code true}, если комментарий принадлежит пользователю, иначе {@code false}.
     * @throws AdNotFoundException если объявление не найдено.
     * @throws CommentNotFoundException если комментарий не найден.
     */
    public boolean isCommentBelongsThisUser(String nameOfAuthenticatedUser, Integer adId, Long commentId) {
        log.info("Проверка на принадлежность объявления текущему аутентифицированному пользователю");

        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);
        List<Comment> foundCommentList = commentRepository.findCommentByAd(ad).orElseThrow(RuntimeException::new);
        List<Comment> collect = foundCommentList.stream().filter(e -> e.getId() == commentId).collect(Collectors.toList());
        Comment foundComment;
        if (!collect.isEmpty()) {
            foundComment = collect.get(0);
        } else {
            throw new CommentNotFoundException();
        }

        return foundComment.getUser().getEmail().equals(nameOfAuthenticatedUser);
    }
}
