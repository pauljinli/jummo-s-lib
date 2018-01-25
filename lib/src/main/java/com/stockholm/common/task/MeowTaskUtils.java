package com.stockholm.common.task;


import android.content.Context;
import android.content.Intent;

public final class MeowTaskUtils {

    private MeowTaskUtils() {

    }

    public static void notifyTaskStop(Context context, MeowTaskModel task) {
        Intent intent = new Intent(TaskConstant.ACTION_TASK_STOP);
        intent.setPackage("com.stockholm.launcher");
        intent.putExtra(TaskConstant.KEY_TASK_MODEL, task);
        context.sendBroadcast(intent);
    }

    public static void notifyTaskComplete(Context context) {
        Intent intent = new Intent(TaskConstant.ACTION_TASK_COMPLETE);
        intent.setPackage("com.stockholm.launcher");
        context.sendBroadcast(intent);
    }
}
