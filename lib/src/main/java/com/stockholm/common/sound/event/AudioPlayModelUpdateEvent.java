package com.stockholm.common.sound.event;


import com.stockholm.common.sound.AudioPlayModel;

public class AudioPlayModelUpdateEvent {

    private int index;
    private AudioPlayModel audioPlayModel;

    public AudioPlayModelUpdateEvent(int index, AudioPlayModel audioPlayModel) {
        this.index = index;
        this.audioPlayModel = audioPlayModel;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AudioPlayModel getAudioPlayModel() {
        return audioPlayModel;
    }

    public void setAudioPlayModel(AudioPlayModel audioPlayModel) {
        this.audioPlayModel = audioPlayModel;
    }

}