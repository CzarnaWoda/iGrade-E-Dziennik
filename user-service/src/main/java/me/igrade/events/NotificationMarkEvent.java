package me.igrade.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.user.response.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMarkEvent {

    private NotificationType notificationType;

    private long notificationReceiverId;
}
