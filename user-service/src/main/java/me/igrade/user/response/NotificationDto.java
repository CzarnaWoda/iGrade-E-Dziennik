package me.igrade.user.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class NotificationDto implements Serializable {

    private NotificationType notificationType;

    private NotificationStatus notificationStatus;

    private String notificationMessage;
}
