package com.stockholm.common.statusbar;

import android.content.Context;
import android.content.Intent;

import com.stockholm.common.Constant;
import com.stockholm.common.utils.StockholmLogger;

public final class StatusManager {

    private static final String TAG = "StatusManager";
    private Context context;
    private Status status;

    private StatusManager(Context context, Status status) {
        this.context = context;
        this.status = status;
    }

    public static void dismissStatus(Context context, int resId) {
        StockholmLogger.i(TAG, "dismiss status: " + context.getPackageName());
        Intent intent = new Intent(StatusConstant.ACTION_STATUS_DISMISS);
        intent.putExtra(Constant.KEY_STATUS_PACKAGE, context.getPackageName());
        intent.putExtra(Constant.KEY_STATUS_RES, resId);
        context.sendBroadcast(intent);
    }

    public void show() {
        StockholmLogger.i(TAG, "show status :" + context.getPackageName());
        Intent intent = new Intent(StatusConstant.ACTION_STATUS_SHOW);
        intent.putExtra(Constant.KEY_STATUS_ENTITY, status);
        context.sendBroadcast(intent);
    }

    public static class Builder {

        Context context;
        String packageName;
        int iconRes;
        int order;
        int group;

        public Builder with(Context context) {
            this.context = context;
            this.packageName = context.getPackageName();
            return this;
        }

        public Builder setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder setGroup(int group) {
            this.group = group;
            return this;
        }

        public StatusManager build() {
            return new StatusManager(context, new Status(this));
        }
    }

}
