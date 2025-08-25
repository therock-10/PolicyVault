package org.godigit.policyvault.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private UUID id;
    private String message;
    private Instant createdAt;
    private boolean read;
}
