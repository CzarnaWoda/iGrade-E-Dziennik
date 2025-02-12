package me.igrade.notifications.teacherstudentdata.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.igrade.notifications.teacherstudentdata.events.NotificationCreateEvent;
import me.igrade.notifications.model.Notification;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.service.impl.NotificationServiceImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationCreateListener {

    private final NotificationServiceImpl notificationService;


    @KafkaListener(topics = "notificationCreateTopic")
    public void handleGradeCreateNotification(NotificationCreateEvent notificationCreateEvent){

        //TODO: Remove debugs and info logs
        //for example e-mail etc.
        final Notification notification = new Notification(notificationCreateEvent.getNotificationType(), NotificationStatus.NOT_CHECKED,notificationCreateEvent.getNotificationMessage(), notificationCreateEvent.getNotificationReceiverId());

        notificationService.createNotification(notification);

        log.info("Notification has been created: " + notification);

    }
}
