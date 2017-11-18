package com.stockholm.common.statusbar;

import android.content.Context;
import android.content.Intent;

import com.stockholm.common.Constant;
import com.stockholm.common.utils.StockholmLogger;

public final class StatusBarManager {

    private static final String TAG = "StatusBarManager";

    private StatusBarManager() {

    }

    public static void showSoundStatus(Context context, String packageName) {
        StockholmLogger.i(TAG, "show status :" + packageName);
        Intent intent = new Intent(StatusAction.ACTION_STATUS_SHOW_MEDIA);
        intent.putExtra(Constant.KEY_PLAYING_PACKAGE, packageName);
        context.sendBroadcast(intent);
    }

    public static void dismissSoundStatus(Context context, String packageName) {
        StockholmLogger.i(TAG, "dismiss status.");
        Intent intent = new Intent(StatusAction.ACTION_STATUS_DISMISS_MEDIA);
        intent.putExtra(Constant.KEY_PLAYING_PACKAGE, packageName);
        context.sendBroadcast(intent);
    }

}
