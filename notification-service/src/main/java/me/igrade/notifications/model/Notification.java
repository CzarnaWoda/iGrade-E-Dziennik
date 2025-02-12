package me.igrade.notifications.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;

@Entity
@RequiredArgsConstructor
@Data
public class Notification {

    //TODO: add date of notification

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;


    private String notificationMessage;

    private long receiverId;


    public Notification(NotificationType notificationType, NotificationStatus notificationStatus, String notificationMessage, long receiverId) {
        this.notificationType = notificationType;
        this.notificationStatus = notificationStatus;
        this.notificationMessage = notificationMessage;
        this.receiverId = receiverId;
    }
}
