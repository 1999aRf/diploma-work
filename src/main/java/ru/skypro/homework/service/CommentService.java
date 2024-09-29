package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.CommentRepository;

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

    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = commentMapper.fromCommentDto(commentDto);
        comment.setUser(getCurrentUser()); // Устанавливаем текущего пользователя как автора
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        // Проверка на авторство
        if (!comment.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to edit this comment.");
        }

        commentMapper.fromCommentDto(commentDto);
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    public boolean isCommentBelongsThisUser(String nameOfAuthenticatedUser, Long adId,Long commentId) {
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
