package com.stockholm.common.notification;


import android.content.Context;
import android.content.Intent;


public final class NotificationManager {

    private NotificationManager() {

    }

    public static void addNotification(Context context, Notification notification) {
        Intent intent = new Intent(NotificationAction.ACTION_ADD_NOTIFICATION_INTENT);
        intent.putExtra(NotificationAction.KEY_NOTIFICATION, notification);
        context.sendBroadcast(intent);
    }

    public static void clearNotification(Context context) {
        Intent intent = new Intent(NotificationAction.ACTION_CLEAR_NOTIFICATION_INTENT);
        context.sendBroadcast(intent);
    }

    public static void pauseNotification(Context context) {
        Intent intent = new Intent(NotificationAction.ACTION_PAUSE_NOTIFICATION_INTENT);
        context.sendBroadcast(intent);
    }

}