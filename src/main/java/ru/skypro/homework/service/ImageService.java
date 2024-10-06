package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.ImageAdRepository;
import ru.skypro.homework.repositories.ImageUserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для управления изображениями, связанными с объявлениями и пользователями.
 * <p>
 * Класс предоставляет функционал для загрузки, сохранения и получения изображений.
 * <p>
 * Логирование осуществляется с помощью аннотации {@link Slf4j}.
 * Используются репозитории {@link ImageAdRepository} и {@link ImageUserRepository} для работы с данными изображений.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageAdRepository imageAdRepository;
    private final ImageUserRepository imageUserRepository;

    @Value("${path.to.imageAd.folder}")
    private String filePathDir;

    /**
     * Создаёт и сохраняет изображение для объявления.
     *
     * @param ad объявление, к которому относится изображение.
     * @param file файл изображения, загруженный пользователем.
     * @return объект {@link ImageAd}, представляющий сохранённое изображение.
     * @throws IOException если произошла ошибка при сохранении файла.
     */
    public ImageAd createImage(Ad ad, MultipartFile file) throws IOException {
        Path filePath = Path.of(filePathDir, ad.getTitle() + "." + getExtensions(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);  // запуск процесса передачи данных
        }
        ImageAd image = new ImageAd();
        image.setAd(ad);
        image.setFilePath(filePath.toString().replace("\\", "/"));
        image.setMediaType(file.getContentType());
        image.setDataForm(file.getBytes());
        image.setFileSize(file.getSize());
        imageAdRepository.save(image);
        log.info("Картинка сохранена в бд");
        return image;
    }

    /**
     * Создаёт и сохраняет изображение для пользователя.
     *
     * @param user пользователь, к которому относится изображение.
     * @param file файл изображения, загруженный пользователем.
     * @throws IOException если произошла ошибка при сохранении файла.
     */
    public void createImage(User user, MultipartFile file) throws IOException {
        Path filePath = Path.of(filePathDir, "/users/" + user.getEmail() + "." + getExtensions(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);  // запуск процесса передачи данных
        }
        ImageUser image = new ImageUser();
        image.setUser(user);
        image.setFilePath(filePath.toString().replace("\\", "/"));
        image.setMediaType(file.getContentType());
        image.setDataForm(file.getBytes());
        image.setFileSize(file.getSize());
        imageUserRepository.save(image);
        log.info("Картинка пользователя сохранена в бд");
    }

    /**
     * Возвращает расширение файла по его имени.
     *
     * @param fileName имя файла.
     * @return строка с расширением файла.
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Сохраняет изображение для объявления в базу данных.
     *
     * @param image объект {@link ImageAd}, представляющий изображение для сохранения.
     * @return сохранённое изображение.
     */
    public ImageAd saveAdImage(ImageAd image) {
        return imageAdRepository.save(image);
    }

    /**
     * Получает изображение объявления по пути к файлу.
     *
     * @param filePath путь к файлу изображения.
     * @param response объект {@link HttpServletResponse} для отправки ответа.
     * @return изображение в виде {@link ResponseEntity} с байтовыми данными.
     * @throws IOException если произошла ошибка при чтении файла.
     */
    public ResponseEntity<byte[]> getImageAd(String filePath, HttpServletResponse response) throws IOException {

        ImageAd imageAd = imageAdRepository.getImageAdByFilePath("/images/" + filePath)
                .orElseThrow(() -> new NoSuchElementException("Нет картинки по заданному пути"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageAd.getMediaType()));
        headers.setContentLength(imageAd.getDataForm().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(imageAd.getDataForm());
    }

    /**
     * Получает изображение пользователя по пути к файлу.
     *
     * @param filePath путь к файлу изображения.
     * @param response объект {@link HttpServletResponse} для отправки ответа.
     * @return изображение в виде {@link ResponseEntity} с байтовыми данными.
     * @throws IOException если произошла ошибка при чтении файла.
     */
    public ResponseEntity<byte[]> getImageUser(String filePath, HttpServletResponse response) throws IOException {

        ImageUser image = imageUserRepository.getImageAdByFilePath("/images/users/" + filePath)
                .orElseThrow(() -> new NoSuchElementException("Нет картинки по заданному пути"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getDataForm().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getDataForm());
    }
}
