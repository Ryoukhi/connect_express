package com.eadl.connect_backend.infrastructure.adapter.out.external.storage;

import com.eadl.connect_backend.domain.port.out.external.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalStorageAdapter implements StorageService {

    private final Path rootLocation;
    private final String baseUrl;

    public LocalStorageAdapter(@Value("${app.storage.location:uploads}") String storageLocation,
                               @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.rootLocation = Paths.get(storageLocation);
        this.baseUrl = baseUrl;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path destinationFile = this.rootLocation.resolve(uniqueFileName);
            Files.write(destinationFile, fileData);
            return getPublicUrl(uniqueFileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String contentType) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path destinationFile = this.rootLocation.resolve(uniqueFileName);
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return getPublicUrl(uniqueFileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String folder, String contentType) {
        try {
            Path folderPath = this.rootLocation.resolve(folder);
            Files.createDirectories(folderPath);
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path destinationFile = folderPath.resolve(uniqueFileName);
            Files.write(destinationFile, fileData);
            return getPublicUrl(folder + "/" + uniqueFileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public byte[] downloadFile(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            Path file = rootLocation.resolve(fileName);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileUrl, e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            Path file = rootLocation.resolve(fileName);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + fileUrl, e);
        }
    }

    @Override
    public String getPublicUrl(String fileName) {
        // Return a relative URL or full URL depending on requirement.
        // Assuming there is a controller serving these files or Nginx.
        // For now, let's return a full URL assuming a static resource handler mapped to /uploads/**
        // Ideally this should be constructed using ServletUriComponentsBuilder if inside a request context,
        // but here we might prefer a configured base URL.
        
        // Simple implementation: return relative path or full path if base url is known
        return baseUrl + "/uploads/" + fileName; 
    }

    @Override
    public String generatePresignedUrl(String fileName, int expirationMinutes) {
        return getPublicUrl(fileName); // Local storage doesn't support signed URLs like S3
    }

    @Override
    public List<String> listFiles(String folder) {
        Path folderPath = this.rootLocation.resolve(folder);
        if (!Files.exists(folderPath)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.walk(folderPath, 1)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files", e);
        }
    }

    @Override
    public boolean fileExists(String fileName) {
        return Files.exists(rootLocation.resolve(fileName));
    }

    @Override
    public Long getFileSize(String fileName) {
        try {
            return Files.size(rootLocation.resolve(fileName));
        } catch (IOException e) {
            return 0L;
        }
    }

    @Override
    public String copyFile(String sourceUrl, String destinationFolder) {
        try {
            String fileName = extractFileNameFromUrl(sourceUrl);
            Path sourceDetails = rootLocation.resolve(fileName);
            Path destFolder = rootLocation.resolve(destinationFolder);
            Files.createDirectories(destFolder);
            Path destFile = destFolder.resolve(fileName);
            Files.copy(sourceDetails, destFile, StandardCopyOption.REPLACE_EXISTING);
            return getPublicUrl(destinationFolder + "/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file", e);
        }
    }

    private String extractFileNameFromUrl(String url) {
        if (url == null) return "";
        // Simple extraction assuming url ends with /uploads/filename
        if (url.contains("/uploads/")) {
            return url.substring(url.lastIndexOf("/uploads/") + 9);
        }
        return url;
    }
}
