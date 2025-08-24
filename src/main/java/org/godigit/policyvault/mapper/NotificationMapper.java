package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.NotificationDto;
import org.godigit.policyvault.entities.Notification;

public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
