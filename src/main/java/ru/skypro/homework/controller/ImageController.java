package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
// @PreAuthorize("permitAll()")
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Operation(
            tags = "Images",
            operationId = "getAdImage",
            summary = "Get ad image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = byte.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{filePath}")
    public ResponseEntity<byte[]> getAdImage(
            @Parameter(name = "id", description = "Ad identifier") @PathVariable(name = "filePath") String filePath,
            HttpServletResponse response
    ) throws IOException {
        return imageService.getImageAd(filePath, response);
    }
    @GetMapping("/users/{filePath}")
    public ResponseEntity<byte[]> getUserImage(
            @Parameter(name = "id", description = "Ad identifier") @PathVariable(name = "filePath") String filePath,
            HttpServletResponse response
    ) throws IOException {
        return imageService.getImageUser(filePath, response);
    }

}
