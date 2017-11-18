package com.stockholm.common.sound.event;


public class AudioPlayProgressEvent {

    private int progress;
    private int currentPosition;
    private int totalDuration;

    public AudioPlayProgressEvent(int progress) {
        this.progress = progress;
    }

    public AudioPlayProgressEvent(int currentPosition, int totalDuration) {
        this.currentPosition = currentPosition;
        this.totalDuration = totalDuration;
    }

    public int getProgress() {
        return progress;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
