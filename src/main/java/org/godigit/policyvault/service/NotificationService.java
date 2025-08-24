package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.NotificationDto;
import org.godigit.policyvault.entities.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createNotification(String message);
    List<Notification> getAllNotifications();
}
