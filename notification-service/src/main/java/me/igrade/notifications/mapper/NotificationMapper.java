package me.igrade.notifications.mapper;


import me.igrade.notifications.dto.NotificationDto;
import me.igrade.notifications.model.Notification;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {


    public NotificationDto mapNotificationToNotificationDTO(Notification notification){

        final NotificationDto notificationDto = new NotificationDto();

        BeanUtils.copyProperties(notification, notificationDto);

        return notificationDto;
    }
}
