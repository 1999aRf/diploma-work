package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.ImageAd;

@Repository
public interface ImageAdRepository extends JpaRepository<ImageAd, Long> {
}
