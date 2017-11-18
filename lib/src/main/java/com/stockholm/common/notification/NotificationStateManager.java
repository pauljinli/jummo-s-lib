package com.stockholm.common.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.stockholm.common.utils.StockholmLogger;

public class NotificationStateManager {
    private static final String TAG = "NotificationStateManager";
    private Context context;
    private boolean showing = false;
    private NotificationStateReceiver notificationStateReceiver;

    public NotificationStateManager(Context context) {
        this.context = context;
    }

    public void registerNightModeReceiver() {
        notificationStateReceiver = new NotificationStateReceiver();
        context.registerReceiver(notificationStateReceiver, new IntentFilter(NotificationAction.ACTION_SHOW_NOTIFICATION));
        context.registerReceiver(notificationStateReceiver, new IntentFilter(NotificationAction.ACTION_DISMISS_NOTIFICATION));
    }

    public void unregisterNightModeReceiver() {
        context.unregisterReceiver(notificationStateReceiver);
    }

    public boolean isShowing() {
        return showing;
    }

    private class NotificationStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NotificationAction.ACTION_SHOW_NOTIFICATION)) {
                StockholmLogger.d(TAG, "show notification");
                showing = true;
            } else if (action.equals(NotificationAction.ACTION_DISMISS_NOTIFICATION)) {
                StockholmLogger.d(TAG, "dismiss notification");
                showing = false;
            }
        }
    }

}