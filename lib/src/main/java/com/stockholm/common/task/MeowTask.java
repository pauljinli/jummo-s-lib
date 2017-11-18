package com.stockholm.common.task;


public interface MeowTask {

    public void start();

    /**
     * called this stop function when receive ACTION_TASK_STOP from launcher.
     */
    public void stop();

    /**
     * called this stop function myself and broadcast this stop action to launcher.
     */
    public void stopSelf();

}
