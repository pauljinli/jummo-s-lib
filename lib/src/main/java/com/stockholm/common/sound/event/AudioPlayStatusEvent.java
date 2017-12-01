package com.stockholm.common.sound.event;


public class AudioPlayStatusEvent {

    public static final int NONE = 0;
    public static final int PLAYING = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;
    public static final int PREPARE = 4;
    public static final int RESTART = 5;
    public static final int BUFFER_STOP = 8;

    public static final int NO_PREVIOUS = 6;
    public static final int NO_NEXT = 7;

    private int status;

    public AudioPlayStatusEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}