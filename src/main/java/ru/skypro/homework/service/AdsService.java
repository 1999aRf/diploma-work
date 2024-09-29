package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdsService {

    private final AdRepository adRepository;
    private final AdMapper adMapper = Mappers.getMapper(AdMapper.class);


    public List<AdDto> getAllAds() {
        return adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
    }
  public List<AdDto> getMyAds() {
        return adRepository.findAll().stream()
                .filter(e -> e.getUser().equals(getCurrentUser()))
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
    }

    public ExtendedAd getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));
        return adMapper.toExtendedAd(ad);
    }

    public AdDto createAd(CreateOrUpdateAdDto adDto) {
        Ad ad = adMapper.fromCreateOrUpdateDto(adDto);
        ad.setUser(getCurrentUser()); // Устанавливаем текущего пользователя как автора
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    public AdDto updateAd(Long id, CreateOrUpdateAdDto adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));

        // Проверка на авторство
        if (!ad.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to edit this ad.");
        }

        adMapper.fromCreateOrUpdateDto(adDto);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));

        // Проверка на авторство
        if (!ad.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to delete this ad.");
        }

        adRepository.delete(ad);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public boolean isAdBelongsThisUser(String nameOfAuthenticatedUser, Long id) {
        log.info("Проверка на принадлежность объявления текущему аутентифицированному пользователю");

        Ad foundAd = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return foundAd.getUser().getEmail().equals(nameOfAuthenticatedUser);
    }
}
