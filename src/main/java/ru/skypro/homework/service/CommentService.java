package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;


    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
        return commentMapper.toCommentDto(comment);
    }

    public CommentDto createComment(Integer adId,CreateOrUpdateCommentDto commentDto) {
        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);

        Comment comment = commentMapper.fromCreateOrUpdateCommentDto(commentDto);
        comment.setUser(getCurrentUser()); // Устанавливаем текущего пользователя как автора
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    public CommentDto updateComment(Long id, CreateOrUpdateCommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        // Проверка на авторство
        if (!comment.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to edit this comment.");
        }

        commentMapper.fromCreateOrUpdateCommentDto(commentDto);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        // Проверка на авторство
        if (!comment.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    public CommentsDto getComments(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        List<Comment> commentList = commentRepository.findCommentByAd(ad).orElseThrow(CommentNotFoundException::new);
        List<CommentDto> dtoList = commentList.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
        return new CommentsDto(dtoList.size(), dtoList);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }
    public boolean isCommentBelongsThisUser(String nameOfAuthenticatedUser, Integer adId,Long commentId) {
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
