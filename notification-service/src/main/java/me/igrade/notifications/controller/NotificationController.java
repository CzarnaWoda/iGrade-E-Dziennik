package me.igrade.notifications.controller;


import lombok.RequiredArgsConstructor;
import me.igrade.notifications.dto.NotificationDto;
import me.igrade.notifications.mapper.NotificationMapper;
import me.igrade.notifications.model.Notification;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;
import me.igrade.notifications.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {


    private final NotificationService notificationService;

    private final NotificationMapper notificationMapper;
    @GetMapping("all/{receiverId}")
    public ResponseEntity<List<NotificationDto>> getAllStudentNotifications(@PathVariable long receiverId){
        final List<Notification> notifications = notificationService.getNotificationsByNotificationReceiverTypeAndReceiverId(receiverId);
        if(notifications.stream().anyMatch(notification -> notification.getNotificationStatus().equals(NotificationStatus.NOT_CHECKED))){
            notificationService.markAsSentNotificationsByReceiverId(receiverId);
        }
        return ResponseEntity.status(OK).body(notifications
                .stream().map(
                        notificationMapper::mapNotificationToNotificationDTO
                ).toList());
    }
    @GetMapping("/not_checked/{receiverId}")
    public ResponseEntity<List<NotificationDto>> getNotCheckedStudentNotifications(@PathVariable long receiverId){
        return ResponseEntity.status(OK).body(notificationService.getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(NotificationStatus.NOT_CHECKED,receiverId)
                .stream().map(
                notificationMapper::mapNotificationToNotificationDTO
        ).toList());
    }

    @GetMapping("/sent/{receiverId}")
    public ResponseEntity<List<NotificationDto>> getSentStudentNotifications(@PathVariable long receiverId){
        return ResponseEntity.status(OK).body(notificationService.getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(NotificationStatus.SENT,receiverId)
                .stream().map(
                        notificationMapper::mapNotificationToNotificationDTO
                ).toList());
    }

    @PostMapping("/sent/{notificationType}/{receiverId}")
    public ResponseEntity<Boolean> studentReceiveNotification(@PathVariable long receiverId, @PathVariable String notificationType){
        if(NotificationType.isNotValidNotificationType(notificationType)){
            return ResponseEntity.status(BAD_REQUEST).body(false);
        }
        notificationService.markAsSentNotificationsByReceiverIdAndNotificationType(receiverId,NotificationType.valueOf(notificationType));

        return ResponseEntity.status(OK).body(true);
    }
    @PostMapping("/checked/{notificationType}/{receiverId}")
    public ResponseEntity<Boolean> studentCheckNotification(@PathVariable long receiverId, @PathVariable String notificationType){
        if(NotificationType.isNotValidNotificationType(notificationType)){
            return ResponseEntity.status(BAD_REQUEST).body(false);
        }
        notificationService.markAsCheckedNotificationsByReceiverIdAndNotificationType(receiverId,NotificationType.valueOf(notificationType));

        return ResponseEntity.status(OK).body(true);
    }
}
