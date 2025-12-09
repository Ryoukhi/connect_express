package com.eadl.connect_backend.domain.model.chat;

public enum MessageType {
    TEXT,           // Message texte simple
    IMAGE,          // Photo (panne, devis, etc.)
    FILE,           // Fichier attaché
    SYSTEM          // Message système (réservation acceptée, etc.)
}