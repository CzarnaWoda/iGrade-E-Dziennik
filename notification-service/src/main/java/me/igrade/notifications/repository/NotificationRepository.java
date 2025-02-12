package me.igrade.notifications.repository;

import me.igrade.notifications.model.Notification;
import me.igrade.notifications.model.enums.NotificationStatus;
import me.igrade.notifications.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Optional<Notification> getNotificationById(long notificationId);


    @Query("SELECT n FROM Notification n where n.receiverId = :receiverId")
    List<Notification> getNotificationsByNotificationReceiverTypeAndReceiverId(
            @Param("receiverId") long receiverId
    );

    @Query("SELECT n FROM Notification n WHERE n.notificationStatus = :notificationStatus AND n.receiverId = :receiverId")
    List<Notification> getNotificationsByNotificationReceiverTypeAndNotificationStatusAndReceiverId(
            @Param("notificationStatus") NotificationStatus notificationStatus,
            @Param("receiverId") long receiverId);

    @Modifying
    @Query("update Notification n SET n.notificationStatus = :notificationStatus WHERE n.receiverId = :receiverId  AND n.notificationType = :notificationType")
    void updateNotificationsByReceiverIdAndNotificationType(
            @Param("receiverId") long receiverId,
            @Param("notificationType")NotificationType notificationType,
            @Param("notificationStatus") NotificationStatus notificationStatus
    );

    @Modifying
    @Query("update Notification n SET n.notificationStatus = :notificationStatus WHERE n.receiverId = :receiverId")
    void updateNotificationsByReceiverId(
            @Param("receiverId") long receiverId,
            @Param("notificationStatus") NotificationStatus notificationStatus
    );
}
