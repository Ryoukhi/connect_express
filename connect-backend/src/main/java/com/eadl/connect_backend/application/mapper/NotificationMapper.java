package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.notification.NotificationResponse;
import com.eadl.connect_backend.domain.model.notification.Notification;

/**
 * Mapper utilitaire pour les notifications
 */
public class NotificationMapper {

    public static NotificationResponse toResponse(Notification notification) {
        if (notification == null) return null;
        NotificationResponse dto = new NotificationResponse();
        dto.setIdNotification(notification.getIdNotification());
        dto.setIdUser(notification.getIdUser());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setData(notification.getData());
        dto.setRead(notification.isRead());
        dto.setReadAt(notification.getReadAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setExpiresAt(notification.getExpiresAt());
        dto.setActionUrl(notification.getActionUrl());
        return dto;
    }

    public static List<NotificationResponse> toResponses(List<Notification> notifications) {
        if (notifications == null) return null;
        return notifications.stream().map(NotificationMapper::toResponse).collect(Collectors.toList());
    }
}
