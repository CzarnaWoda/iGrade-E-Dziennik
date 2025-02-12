package me.igrade.notifications.teacherstudentdata.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.notifications.model.enums.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateEvent {

    private NotificationType notificationType;
    private String notificationMessage;
    private long notificationReceiverId;

}