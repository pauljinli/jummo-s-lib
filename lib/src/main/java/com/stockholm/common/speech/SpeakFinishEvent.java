package com.stockholm.common.speech;


public class SpeakFinishEvent {
    private long speakId;

    public SpeakFinishEvent(long speakId) {
        setSpeakId(speakId);
    }

    public long getSpeakId() {
        return speakId;
    }

    public void setSpeakId(long speakId) {
        this.speakId = speakId;
    }

}