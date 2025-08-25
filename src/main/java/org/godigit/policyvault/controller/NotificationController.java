package org.godigit.policyvault.controller;

import lombok.RequiredArgsConstructor;
import org.godigit.policyvault.dto.NotificationDto;
import org.godigit.policyvault.entities.Notification;
import org.godigit.policyvault.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST API for managing notifications.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    @PreAuthorize("hasRole('DEPT_HEAD')")
    public ResponseEntity<List<Notification>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
