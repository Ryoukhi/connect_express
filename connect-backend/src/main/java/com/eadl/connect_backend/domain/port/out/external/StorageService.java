package com.eadl.connect_backend.domain.port.out.external;

import java.io.InputStream;
import java.util.List;

/**
 * Port OUT - Service de stockage
 * Interface pour le stockage de fichiers (S3, Azure Blob, etc.)
 */
public interface StorageService {
    
    /**
     * Upload un fichier
     */
    String uploadFile(byte[] fileData, String fileName, String contentType);
    
    /**
     * Upload un fichier depuis un stream
     */
    String uploadFile(InputStream inputStream, String fileName, String contentType);
    
    /**
     * Upload un fichier dans un dossier spécifique
     */
    String uploadFile(byte[] fileData, String fileName, String folder, String contentType);
    
    /**
     * Télécharge un fichier
     */
    byte[] downloadFile(String fileUrl);
    
    /**
     * Supprime un fichier
     */
    boolean deleteFile(String fileUrl);
    
    /**
     * Récupère l'URL publique d'un fichier
     */
    String getPublicUrl(String fileName);
    
    /**
     * Génère une URL signée temporaire
     */
    String generatePresignedUrl(String fileName, int expirationMinutes);
    
    /**
     * Liste les fichiers d'un dossier
     */
    List<String> listFiles(String folder);
    
    /**
     * Vérifie si un fichier existe
     */
    boolean fileExists(String fileName);
    
    /**
     * Récupère la taille d'un fichier
     */
    Long getFileSize(String fileName);
    
    /**
     * Copie un fichier
     */
    String copyFile(String sourceUrl, String destinationFolder);
}
