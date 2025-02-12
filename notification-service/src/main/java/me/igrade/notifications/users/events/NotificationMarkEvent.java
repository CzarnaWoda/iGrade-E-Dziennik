package me.igrade.notifications.users.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.notifications.model.enums.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMarkEvent {

    private NotificationType notificationType;

    private long notificationReceiverId;
}
