package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateComment {

    private LocalDateTime localDateTime;
    private String text;
    private long userId;
    private long adId;
}