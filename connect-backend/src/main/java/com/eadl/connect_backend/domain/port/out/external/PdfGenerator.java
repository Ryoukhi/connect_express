package com.eadl.connect_backend.domain.port.out.external;

/**
 * Port OUT - Générateur PDF
 * Interface pour la génération de documents PDF
 */
public interface PdfGenerator {
    
    
    /**
     * Génère un PDF depuis un template HTML
     */
    byte[] generatePdfFromHtml(String htmlContent);
    
    // /**
    //  * Convertit un document en PDF
    //  */
    // byte[] convertToPdf(byte[] documentData, String format);
}