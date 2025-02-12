package me.igrade.notifications.dto;

import lombok.Data;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;

import java.io.Serializable;


@Data
public class NotificationDto implements Serializable {

    private NotificationType notificationType;

    private NotificationStatus notificationStatus;

    private String notificationMessage;
}
