package me.igrade.notifications.service;

import me.igrade.notifications.model.Notification;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationService {


    void createNotification(Notification notification);
    Optional<Notification> getNotificationById(long notificationId);
    List<Notification> getNotificationsByNotificationReceiverTypeAndReceiverId(long receiverId);
    List<Notification> getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(NotificationStatus notificationStatus, long receiverId);
    void markAsSentNotificationsByReceiverIdAndNotificationType(long receiverId, NotificationType notificationType);
    void markAsSentNotificationsByReceiverId(long receiverId);
    void markAsCheckedNotificationsByReceiverIdAndNotificationType(long receiverId, NotificationType notificationType);
}
