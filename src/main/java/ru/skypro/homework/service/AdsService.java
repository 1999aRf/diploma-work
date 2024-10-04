package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exceptions.UserNotAuthenticatedException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.ImageAdRepository;
import ru.skypro.homework.repositories.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageAdRepository imageAdRepository;
    private final ImageService imageService;
    private final AdMapper adMapper = Mappers.getMapper(AdMapper.class);

    @Value("${path.to.imageAd.folder}")
    private String filePathDir;


    public AdsDto getAllAds() {
        List<AdDto> collect = adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new AdsDto(collect);
    }
  public AdsDto getMyAds() {
      List<AdDto> collect = adRepository.findAll().stream()
              .filter(e -> e.getUser().equals(getCurrentUser()))
              .map(adMapper::toAdDto)
              .collect(Collectors.toList());
      return new AdsDto(collect);
    }

    public ExtendedAd getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));
        return adMapper.toExtendedAd(ad);
    }

    public AdDto createAd(CreateOrUpdateAdDto adDto, MultipartFile file,Authentication authentication) throws IOException {
        if (!authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("Для добавления объявления необходима аутентификация");
        }
        log.info("Перед маппером");
        Ad ad = adMapper.fromCreateOrUpdateDto(adDto);
        ad.setUser(getCurrentUser());
        log.info("Сработал маппер");

        ImageAd image = imageService.createImage(ad, file);
        ad.setUser(getCurrentUser()); // Устанавливаем текущего пользователя как автора
        ad.setImageAd(image);
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
        log.info("Вызван метод удаления объявления");
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));

        // Проверка на авторство
        if (!ad.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to delete this ad.");
        }

        adRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }

    public boolean isAdBelongsThisUser(String nameOfAuthenticatedUser, Long id) {
        log.info("Проверка на принадлежность объявления текущему аутентифицированному пользователю");

        Ad foundAd = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return foundAd.getUser().getEmail().equals(nameOfAuthenticatedUser);
    }

    }





