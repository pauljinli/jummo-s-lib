package com.stockholm.common.view;


public class KeyAction {
    private static final long MULTI_PRESS_DISTANCE = 500;

    private long time;

    public KeyAction() {
        setTime(System.currentTimeMillis());
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isMultiAction(KeyAction keyAction) {
        return keyAction != null && (time - keyAction.getTime()) <= MULTI_PRESS_DISTANCE;
    }

}