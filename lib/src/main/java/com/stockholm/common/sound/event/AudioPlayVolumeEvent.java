package com.stockholm.common.sound.event;


public class AudioPlayVolumeEvent {

    private float volume;

    public AudioPlayVolumeEvent(float volume) {
        this.volume = volume;
    }

    public float getVolume() {
        return volume;
    }
}
