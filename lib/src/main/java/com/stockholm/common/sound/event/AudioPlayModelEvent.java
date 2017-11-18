package com.stockholm.common.sound.event;


import com.stockholm.common.sound.AudioPlayModel;

public class AudioPlayModelEvent<T extends AudioPlayModel> {

    private T model;
    private int index;

    public AudioPlayModelEvent(T model) {
        this.model = model;
    }

    public AudioPlayModelEvent(T model, int index) {
        this.model = model;
        this.index = index;
    }

    public T getModel() {
        return model;
    }

    public int getIndex() {
        return index;
    }

}