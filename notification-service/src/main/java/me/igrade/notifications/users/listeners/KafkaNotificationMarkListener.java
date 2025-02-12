package me.igrade.notifications.users.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.igrade.notifications.service.impl.NotificationServiceImpl;
import me.igrade.notifications.users.events.NotificationMarkEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationMarkListener {

    private final NotificationServiceImpl notificationService;

    @KafkaListener(topics = "notificationMarkTopic")
    public void handleMarkUsersNotifications(NotificationMarkEvent notificationMarkEvent){
        notificationService.markAsCheckedNotificationsByReceiverIdAndNotificationType(notificationMarkEvent.getNotificationReceiverId(), notificationMarkEvent.getNotificationType());

        log.info("DEBUG INFORMATION -> Mark as read notifications from event:" + notificationMarkEvent);
    }
}
