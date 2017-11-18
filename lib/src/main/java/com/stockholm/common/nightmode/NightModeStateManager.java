package com.stockholm.common.nightmode;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.stockholm.common.IntentExtraKey;

public class NightModeStateManager {

    private Context context;
    private boolean showing = false;
    private NightModeStateReceiver nightModeStateReceiver;

    public NightModeStateManager(Context context) {
        this.context = context;
    }

    public void registerNightModeReceiver() {
        nightModeStateReceiver = new NightModeStateReceiver();
        context.registerReceiver(nightModeStateReceiver, new IntentFilter(IntentExtraKey.ACTION_SHOW_NIGHT_MODE));
        context.registerReceiver(nightModeStateReceiver, new IntentFilter(IntentExtraKey.ACTION_CLEAR_NIGHT_MODE));
        context.registerReceiver(nightModeStateReceiver, new IntentFilter(IntentExtraKey.ACTION_ANY_KEY_PRESSED));
    }

    public void unregisterNightModeReceiver() {
        context.unregisterReceiver(nightModeStateReceiver);
    }

    public boolean isShowing() {
        return showing;
    }

    private class NightModeStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(IntentExtraKey.ACTION_SHOW_NIGHT_MODE)) {
                showing = true;
            } else if (action.equals(IntentExtraKey.ACTION_CLEAR_NIGHT_MODE)
                    || action.equals(IntentExtraKey.ACTION_ANY_KEY_PRESSED)) {
                showing = false;
            }
        }
    }
}
