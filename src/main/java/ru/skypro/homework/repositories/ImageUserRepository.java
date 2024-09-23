package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.ImageUser;

@Repository
public interface ImageUserRepository extends JpaRepository<ImageUser, Long> {
}
