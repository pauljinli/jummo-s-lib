package com.stockholm.common.sound.event;


public class AudioPlayHistoryEvent {

    private int index;
    private int historyProgress;

    public AudioPlayHistoryEvent() {
    }

    public AudioPlayHistoryEvent(int index, int historyProgress) {
        this.index = index;
        this.historyProgress = historyProgress;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHistoryProgress() {
        return historyProgress;
    }

    public void setHistoryProgress(int historyProgress) {
        this.historyProgress = historyProgress;
    }

}