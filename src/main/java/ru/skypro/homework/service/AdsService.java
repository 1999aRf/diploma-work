package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями (Ads).
 * Предоставляет методы для создания, обновления, удаления и получения объявлений.
 * <p>
 * Использует репозитории {@link AdRepository}, {@link UserRepository}, {@link ImageAdRepository}
 * и мапперы {@link AdMapper} для преобразования данных.
 * <p>
 * Класс аннотирован как {@link Service}, что позволяет использовать его как сервисный компонент в Spring.
 * Логирование осуществляется через аннотацию {@link Slf4j}.
 */
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

    /**
     * Возвращает список всех объявлений.
     *
     * @return объект {@link AdsDto}, содержащий список всех объявлений.
     */
    public AdsDto getAllAds() {
        List<AdDto> collect = adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new AdsDto(collect);
    }

    /**
     * Возвращает список объявлений текущего пользователя.
     *
     * @return объект {@link AdsDto}, содержащий список объявлений текущего пользователя.
     */
    public AdsDto getMyAds() {
        List<AdDto> collect = adRepository.findAll().stream()
                .filter(e -> e.getUser().equals(getCurrentUser()))
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new AdsDto(collect);
    }

    /**
     * Возвращает объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект {@link ExtendedAd}, содержащий расширенную информацию об объявлении.
     * @throws NoSuchElementException если объявление не найдено.
     */
    public ExtendedAd getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));
        return adMapper.toExtendedAd(ad);
    }

    /**
     * Создает новое объявление для текущего пользователя.
     *
     * @param adDto         данные для создания объявления.
     * @param file          файл изображения для объявления.
     * @param authentication объект для проверки аутентификации пользователя.
     * @return объект {@link AdDto}, содержащий данные созданного объявления.
     * @throws IOException если возникла ошибка при загрузке изображения.
     * @throws UserNotAuthenticatedException если пользователь не аутентифицирован.
     */
    public AdDto createAd(CreateOrUpdateAdDto adDto, MultipartFile file, Authentication authentication) throws IOException {
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

    /**
     * Обновляет существующее объявление по его идентификатору.
     *
     * @param id    идентификатор объявления.
     * @param adDto данные для обновления объявления.
     * @return объект {@link AdDto}, содержащий обновленные данные объявления.
     * @throws NoSuchElementException если объявление не найдено.
     */
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));

        adMapper.updateAdFromDto(adDto, ad);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @throws NoSuchElementException если объявление не найдено.
     */
    public void deleteAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad not found"));

        adRepository.deleteById(id);
        log.info("запрос на удаление произведен успешно");
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
     * Проверяет, принадлежит ли объявление текущему аутентифицированному пользователю.
     *
     * @param nameOfAuthenticatedUser имя текущего аутентифицированного пользователя.
     * @param id                      идентификатор объявления.
     * @return {@code true}, если объявление принадлежит пользователю, иначе {@code false}.
     */
    public boolean isAdBelongsThisUser(String nameOfAuthenticatedUser, Integer id) {
        log.info("Проверка на принадлежность объявления текущему аутентифицированному пользователю");

        Ad foundAd = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return foundAd.getUser().getEmail().equals(nameOfAuthenticatedUser);
    }

}