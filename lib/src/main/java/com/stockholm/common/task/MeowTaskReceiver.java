package com.stockholm.common.task;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MeowTaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TaskBean taskBean = intent.getParcelableExtra(TaskConstant.KEY_TASK_MODEL);
        if (intent.getAction().equals(TaskConstant.ACTION_START_TASK)) {
            onTaskStart(context, taskBean);
        } else if (intent.getAction().equals(TaskConstant.ACTION_STOP_TASK)) {
            onTaskStop(context, taskBean);
        }
    }

    public void onTaskStart(Context context, TaskBean taskBean) {

    }

    public void onTaskStop(Context context, TaskBean taskBean) {

    }
}
