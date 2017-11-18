package com.stockholm.common.task;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class MeowTaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MeowTaskModel model = intent.getParcelableExtra(TaskConstant.KEY_TASK_MODEL);
        String time = intent.getStringExtra(TaskConstant.KEY_TASK_START_TIME);
        LocalTime startTime = null;
        if (!TextUtils.isEmpty(time)) {
            startTime = LocalTime.parse(time, DateTimeFormat.forPattern(TaskConstant.VALUE_TASK_START_TIME_FORMAT));
        }
        if (intent.getAction().equals(TaskConstant.ACTION_START_TASK)) {
            onTaskStart(context, model, startTime);
        } else if (intent.getAction().equals(TaskConstant.ACTION_STOP_TASK)) {
            onTaskStop(context, model, startTime);
        }
    }

    public void onTaskStart(Context context, MeowTaskModel model, LocalTime startTime) {

    }

    public void onTaskStop(Context context, MeowTaskModel model, LocalTime startTime) {

    }
}
