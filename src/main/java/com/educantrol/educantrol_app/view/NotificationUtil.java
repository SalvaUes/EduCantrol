package com.educantrol.educantrol_app.view;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtil {

    // Método para notificaciones de éxito
    public static void showSuccess(String message) {
        Notification notification = new Notification(message, 3000, Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS); // Color verde
        notification.open();
    }

    // Método para notificaciones de error
    public static void showError(String message) {
        Notification notification = new Notification(message, 3000, Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR); // Color rojo
        notification.open();
    }
}