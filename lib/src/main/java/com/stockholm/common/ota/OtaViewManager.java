package com.stockholm.common.ota;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.stockholm.common.IntentExtraKey;

public class OtaViewManager {

    private Context context;
    private boolean showing = false;
    private OtaViewReceiver otaViewReceiver;

    public OtaViewManager(Context context) {
        this.context = context;
    }

    public void registerOtaViewReceiver() {
        otaViewReceiver = new OtaViewReceiver();
        context.registerReceiver(otaViewReceiver, new IntentFilter(IntentExtraKey.ACTION_SHOW_OTA_VIEW));
        context.registerReceiver(otaViewReceiver, new IntentFilter(IntentExtraKey.ACTION_CLEAR_OTA_VIEW));
    }

    public void unregisterOtaViewReceiver() {
        context.unregisterReceiver(otaViewReceiver);
    }

    public boolean isShowing() {
        return showing;
    }

    private class OtaViewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(IntentExtraKey.ACTION_SHOW_OTA_VIEW)) {
                showing = true;
            } else if (action.equals(IntentExtraKey.ACTION_CLEAR_OTA_VIEW)) {
                showing = false;
            }
        }
    }
}
