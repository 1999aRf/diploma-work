package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdsDto {
    private int count;
    List<AdDto> results;

    public AdsDto(List<AdDto> results) {
        this.count = results.size();
        this.results = results;
    }
}
