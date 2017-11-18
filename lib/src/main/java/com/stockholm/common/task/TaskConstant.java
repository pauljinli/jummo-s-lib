package com.stockholm.common.task;


public final class TaskConstant {

    // launcher send action to app.
    public static final String ACTION_START_TASK = "action.stockholm.START_TASK";
    public static final String ACTION_STOP_TASK = "action.stockholm.STOP_TASK";

    // app send action to launcher.
    public static final String ACTION_TASK_STOP = "action.stockholm.TASK_STOP";

//    public static final String KEY_TASK_ID = "key_meow_task_id";
//    public static final String KEY_TASK_PACKAGE = "key_meow_task_package";
    public static final String KEY_TASK_START_TIME = "key_meow_task_start_time";
    public static final String VALUE_TASK_START_TIME_FORMAT = "HH:mm";

    public static final String KEY_TASK_MODEL = "key_task_model";

    private TaskConstant() {

    }
}
