package com.stockholm.common.sound.event;


public class AudioPlayControlEvent {

    public static final int TOGGLE = 1;
    public static final int NEXT = 2;
    public static final int PREVIOUS = 3;
    public static final int PLAY = 4;

    public static final int PAUSE = 5;
    public static final int RESTART = 6;

    public static final int STOP = 7;

    private int cmd;

    public AudioPlayControlEvent(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }
}
