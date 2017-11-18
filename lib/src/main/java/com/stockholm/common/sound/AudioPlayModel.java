package com.stockholm.common.sound;

public class AudioPlayModel {

    private String id;
    private String audioUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append("\t");
        sb.append("audioUrl:").append(audioUrl).append("\t");
        return sb.toString();
    }
}
