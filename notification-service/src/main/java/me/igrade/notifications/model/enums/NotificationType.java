package me.igrade.notifications.model.enums;

public enum NotificationType {

    GRADE,NOTE,CLASS,SUBJECT,OTHER;



    public static boolean isNotValidNotificationType(String type) {
        try {
            NotificationType.valueOf(type);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
