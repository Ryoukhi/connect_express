package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.TechnicianDocumentDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;

public class TechnicianDocumentMapper {

    private TechnicianDocumentMapper() {
        // Utility class
    }

    public static TechnicianDocumentDto toDto(TechnicianDocument document) {
        if (document == null) return null;

        TechnicianDocumentDto dto = new TechnicianDocumentDto();
        dto.setIdDocument(document.getIdDocument());
        dto.setIdProfile(document.getIdProfile());
        dto.setType(document.getType());
        dto.setUrl(document.getUrl());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setVerified(document.isVerified());
        dto.setVerificationNote(document.getVerificationNote());

        return dto;
    }

    public static TechnicianDocument toModel(TechnicianDocumentDto dto) {
        if (dto == null) return null;

        TechnicianDocument document = new TechnicianDocument();
        document.setIdDocument(dto.getIdDocument());
        document.setIdProfile(dto.getIdProfile());
        document.setType(dto.getType());
        document.setUrl(dto.getUrl());
        document.setUploadedAt(dto.getUploadedAt());
        document.setVerified(dto.isVerified());
        document.setVerificationNote(dto.getVerificationNote());

        return document;
    }

    public static List<TechnicianDocumentDto> toDtoList(List<TechnicianDocument> documents) {
        return documents.stream()
                .map(TechnicianDocumentMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<TechnicianDocument> toModelList(List<TechnicianDocumentDto> dtos) {
        return dtos.stream()
                .map(TechnicianDocumentMapper::toModel)
                .collect(Collectors.toList());
    }
}