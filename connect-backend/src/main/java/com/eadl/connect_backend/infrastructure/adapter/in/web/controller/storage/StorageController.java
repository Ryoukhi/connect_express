package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.storage;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eadl.connect_backend.domain.port.out.external.StorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@Tag(name = "Storage", description = "Gestion du stockage de fichiers (Upload public)")
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "Upload un fichier (Public)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fichier uploadé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"url\": \"https://...\"}"))),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            @Parameter(description = "Fichier à uploader") @RequestParam("file") MultipartFile file
    ) {
        try {
            String url = storageService.uploadFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
