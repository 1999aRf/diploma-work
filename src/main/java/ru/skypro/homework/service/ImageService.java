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
import ru.skypro.homework.repositories.ImageAdRepository;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageAdRepository imageAdRepository;
    @Value("${path.to.imageAd.folder}")
    private String filePathDir;

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
        image.setFilePath(filePath.toString());
        image.setMediaType(file.getContentType());
        image.setDataForm(file.getBytes());
        image.setFileSize(file.getSize());
        imageAdRepository.save(image);
        log.info("Картинка сохранена в бд");
        return image;
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public ImageAd saveAdImage(ImageAd image) {
        return imageAdRepository.save(image);
    }

    public ResponseEntity<byte[]> getImageAd(String filePath, HttpServletResponse response) throws IOException {

        ImageAd imageAd = imageAdRepository.getImageAdByFilePath("\\images\\" + filePath)
                .orElseThrow(() -> new NoSuchElementException("Нет картинки по заданному пути"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageAd.getMediaType()));
        headers.setContentLength(imageAd.getDataForm().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(imageAd.getDataForm());


    }
}
