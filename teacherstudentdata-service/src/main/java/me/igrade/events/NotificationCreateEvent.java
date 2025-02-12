package me.igrade.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateEvent {

    private NotificationReceiverType notificationReceiverType;
    private NotificationType notificationType;
    private String notificationMessage;
    private long notificationReceiverId;

}