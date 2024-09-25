package ru.skypro.homework.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {
    private int count;
    private List<CommentDto> results;

    public CommentsDto(List<CommentDto> results) {
        this.count = results.size();
        this.results = results;
    }
}
