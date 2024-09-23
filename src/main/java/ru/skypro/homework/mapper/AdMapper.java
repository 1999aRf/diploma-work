package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "author"),
            @Mapping(source = "imageAd.filePath", target = "image"),
            @Mapping(source = "id", target = "pk"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "title", target = "title")
    })
    AdDto toAdDto(Ad ad);

    @Mappings({
            @Mapping(source = "author", target = "user.id"),
            @Mapping(source = "image", target = "imageAd.filePath"),
            @Mapping(source = "pk", target = "id"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "title", target = "title")
    })
    Ad fromAdDto(AdDto dto);

    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "description", target = "description")
    })
    Ad fromCreateOrUpdateDto(CreateOrUpdateAdDto dto);


    @Mappings({
            @Mapping(source = "id", target = "pk"),
            @Mapping(source = "user.firstName", target = "authorFirstName"),
            @Mapping(source = "user.lastName", target = "authorLastName"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "user.email", target = "email"),
            @Mapping(source = "imageAd.filePath", target = "image"),
            @Mapping(source = "user.phone", target = "phone"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "title", target = "title")
    })
    ExtendedAd toExtendedAd(Ad ad);

    @Mappings({
            @Mapping(source = "pk", target = "id"),
            @Mapping(source = "authorFirstName", target = "user.firstName"),
            @Mapping(source = "authorLastName", target = "user.lastName"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "email", target = "user.email"),
            @Mapping(source = "image", target = "imageAd.filePath"),
            @Mapping(source = "phone", target = "user.phone"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "title", target = "title")
    })
    Ad fromExtendedAd(ExtendedAd dto);



}
