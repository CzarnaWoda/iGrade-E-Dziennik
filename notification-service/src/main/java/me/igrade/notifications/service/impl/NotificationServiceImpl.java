package me.igrade.notifications.service.impl;


import lombok.RequiredArgsConstructor;
import me.igrade.notifications.model.Notification;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;
import me.igrade.notifications.repository.NotificationRepository;
import me.igrade.notifications.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;

    @Override
    public void createNotification(Notification notification){

        notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> getNotificationById(long notificationId){
        return notificationRepository.getNotificationById(notificationId);
    }

    @Override
    public List<Notification> getNotificationsByNotificationReceiverTypeAndReceiverId(long receiverId){
        return notificationRepository.getNotificationsByNotificationReceiverTypeAndReceiverId(receiverId);
    }

    @Override
    public List<Notification> getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(NotificationStatus notificationStatus, long receiverId){
        return notificationRepository.getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(notificationStatus,receiverId);
    }

    @Override
    @Transactional
    public void markAsSentNotificationsByReceiverIdAndNotificationType(long receiverId, NotificationType notificationType){
        notificationRepository.updateNotificationsByReceiverIdAndNotificationType(receiverId, notificationType,NotificationStatus.SENT);
    }

    @Override
    @Transactional
    public void markAsSentNotificationsByReceiverId(long receiverId){
        notificationRepository.updateNotificationsByReceiverId(receiverId,NotificationStatus.SENT);
    }

    @Override
    @Transactional
    public void markAsCheckedNotificationsByReceiverIdAndNotificationType(long receiverId, NotificationType notificationType){
        notificationRepository.updateNotificationsByReceiverIdAndNotificationType(receiverId, notificationType,NotificationStatus.CHECKED);
    }


}
