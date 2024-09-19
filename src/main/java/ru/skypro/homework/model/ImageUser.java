package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "image_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image_user")
    private long id;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Lob
    @Column(name = "data_form")
    @JsonIgnore
    private byte[] dataForm;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;
}
